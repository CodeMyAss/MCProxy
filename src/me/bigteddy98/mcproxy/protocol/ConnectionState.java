package me.bigteddy98.mcproxy.protocol;

public enum ConnectionState {
	HANDSHAKE(0), STATUS(1), LOGIN(2), PLAY(3);

	private final int id;

	private ConnectionState(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	private static final ConnectionState[] array = new ConnectionState[4];

	static {
		for (ConnectionState state : values()) {
			array[state.getId()] = state;
		}
	}
	
	public static ConnectionState fromId(int id){
		return array[id];
	}
}
