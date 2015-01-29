package com.a2client.network.login.serverpackets;

import com.a2client.Config;
import com.a2client.network.login.clientpackets.Login;

public class Init extends LoginServerPacket
{
    private int server_proto_version;

    @Override
    public void readImpl()
    {
        server_proto_version = readC();
    }

    @Override
    public void run()
    {
        if (Config.PROTO_VERSION == server_proto_version)
        {
            getConnect().sendPacket(
                    new Login(com.a2client.screens.Login._account, com.a2client.screens.Login._password));
        }
        else
        {
            com.a2client.screens.Login.Error("proto_version");
        }

    }
}
