package com.a2client.network.login.clientpackets;

import com.a2client.network.Net;
import com.a2client.util.network.BaseSendPacket;

public abstract class LoginClientPacket extends BaseSendPacket
{
    public void Send()
    {
        Net.getConnection().sendPacket(this);
    }
}
