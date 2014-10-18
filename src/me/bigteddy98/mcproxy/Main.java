/* 
 * MCProxy
 * Copyright (C) 2014 Sander Gielisse
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.bigteddy98.mcproxy;

import java.util.concurrent.ThreadFactory;

import me.bigteddy98.mcproxy.protocol.ClientboundConnectionInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Main {

	public static final String NAME = "MCProxy";
	public static final String VERSION = "0.0.1";
	public static final String PROTOCOL_VERSION = "1.8";
	public static final int EXCEPTED_TICK_RATE = 20;
	public static final int EXCEPTED_SLEEP_TIME = 1000 / EXCEPTED_TICK_RATE;
	public static final int CANT_KEEP_UP_TIMEOUT = -10000;
	public static final int MAX_NETTY_BOSS_THREADS = 4;
	public static final int MAX_NETTY_WORKER_THREADS = 8;
	public static final int MAX_SLEEP = 100;
	public static final int DEFAULT_PROTOCOL = 47;
	public static final String AUTHOR = "Sander Gielisse || BigTeddy98";

	private final int fromPort;
	private final int toPort;

	public Main() {
		this.fromPort = 25566;
		this.toPort = 25565;
	}

	public void run() throws Exception {
		ProxyLogger.info("Starting " + NAME + " version " + VERSION + " developed by " + AUTHOR + "!");

		final ThreadGroup nettyListeners = new ThreadGroup(Thread.currentThread().getThreadGroup(), "Netty Listeners");
		new Thread(nettyListeners, new Runnable() {

			@Override
			public void run() {
				ProxyLogger.info("Started Netty Server at port " + fromPort + "...");
				final ThreadGroup group = new ThreadGroup(nettyListeners, "Listener-" + toPort);
				EventLoopGroup bossGroup = new NioEventLoopGroup(MAX_NETTY_BOSS_THREADS, new ThreadFactory() {

					private int threadCount = 0;
					private String newName = group.getName() + "\\boss";

					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(group, r, newName + "\\" + threadCount++);
						t.setPriority(Thread.NORM_PRIORITY - 1);
						t.setDaemon(true);
						return t;
					}
				});
				EventLoopGroup workerGroup = new NioEventLoopGroup(MAX_NETTY_WORKER_THREADS, new ThreadFactory() {

					private int threadCount = 0;
					private String newName = group.getName() + "\\worker";

					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(group, r, newName + "\\" + threadCount++);
						t.setPriority(Thread.NORM_PRIORITY - 1);
						t.setDaemon(true);
						return t;
					}
				});
				try {
					ServerBootstrap bootstrab = new ServerBootstrap();
					bootstrab.group(bossGroup, workerGroup);
					bootstrab.channel(NioServerSocketChannel.class);
					bootstrab.childHandler(new ClientboundConnectionInitializer("localhost", toPort));
					bootstrab.childOption(ChannelOption.AUTO_READ, false);
					bootstrab.bind(fromPort).sync().channel().closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					bossGroup.shutdownGracefully();
					workerGroup.shutdownGracefully();
				}
			}
		}).start();
	}

	public static void main(String[] args) throws Exception {
		new Main().run();
	}
}