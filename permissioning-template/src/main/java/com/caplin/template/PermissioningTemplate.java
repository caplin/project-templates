package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.permissioning.User;
import com.caplin.permissioning.*;

import java.util.Collections;
import java.util.Map;

public class PermissioningTemplate {

    private final DataSource dataSource;

    public PermissioningTemplate(DataSource dataSource) {
        this.dataSource = dataSource;


        PermissioningDataSource permsDS = new PermissioningDataSource(dataSource, Role.MASTER);


        permsDS.startImageTransaction();
        /* Permissioning data should initially be added as part of an image transaction. This can be done
         * before the Permissioning DataSource starts or after it has started. Subsequent changes in
         * permissioning data should be done as an update transaction. */

        User adminUser = permsDS.createUser("admin", "admin");
        adminUser.applyPermission(Collections.singleton("/.*"), Constants.DEFAULT_PERMISSION_NAMESPACE, "VIEW", Authorization.ALLOW);

        Map<String, String> espTradeFields = Collections.singletonMap("TradingProtocol", "ESP");
        permsDS.createActionRule("/TEMPLATE/TRADE/%U", espTradeFields, Constants.DEFAULT_PERMISSION_NAMESPACE, "FX-ESP-TRADE", "Instrument");

        adminUser.applyPermission(Collections.singleton("/.*"), Constants.DEFAULT_PERMISSION_NAMESPACE, "FX-ESP-TRADE", Authorization.ALLOW);


        permsDS.commitTransaction();
    }

    public static void main(String[] args) {
        DataSource dataSource = DataSource.fromArgs(args);

        new PermissioningTemplate(dataSource);

        dataSource.start();
    }
}
