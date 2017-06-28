package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.permissioning.PermissioningDataSource;
import com.caplin.permissioning.Role;

public class PermissioningAdapterPermissioningProvider {

	public PermissioningAdapterPermissioningProvider(DataSource dataSource) {
		PermissioningDataSource permissioningDataSource = new PermissioningDataSource(dataSource, Role.MASTER);

		permissioningDataSource.startImageTransaction();
		/* Permissioning data should initially be added as part of an image transaction. This
         * can be done before the Permissioning DataSource starts or after it has started.
         * Subsequent changes in permissioning data should be done as an update transaction.
         */
		permissioningDataSource.commitTransaction();
	}
}
