package com.caplin.template;

import java.util.logging.Level;

import com.caplin.datasource.ConnectionListener;
import com.caplin.datasource.DataSource;
import com.caplin.datasource.DataSourceFactory;
import com.caplin.datasource.PeerStatusEvent;

public class PermissioningAdapter implements ConnectionListener {
	private DataSource dataSource;

	public PermissioningAdapter(DataSource dataSource) throws Exception {
		this.dataSource = dataSource;

		new PermissioningAdapterPermissioningProvider(this.dataSource);

		this.dataSource.addConnectionListener(this);
	}

	/**
	 * ConnectionListener implementation
	 **/
	@Override
	public void onPeerStatus(PeerStatusEvent peerStatusEvent) {
		dataSource.getLogger().log(Level.INFO, "Peer status received: peer={0}, status={1}", new Object[]{peerStatusEvent.getPeer(), peerStatusEvent.getPeerStatus()});
	}

	public static void main(String[] args) throws Exception {
		DataSource dataSource = DataSource.fromArgs(args);
		new PermissioningAdapter(dataSource);
		dataSource.start();
	}
}
