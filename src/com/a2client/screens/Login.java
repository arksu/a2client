package com.a2client.screens;

import com.a2client.Config;
import com.a2client.Main;
import com.a2client.Input;
import com.a2client.Lang;
import com.a2client.gui.*;
import com.a2client.network.Net;
import com.a2client.network.netty.NettyConnection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;

public class Login extends BaseScreen
{
    static public String _account;
    static public String _password;
    static private String _status = "";

    static public String _gameserver_host;
    static public int _gameserver_port;
    static public int _gameserver_key1;
    static public int _gameserver_key2;

    /**
     * gui контролы экрана логина
     */
    GUI_Button btn_login, btn_exit, btn_options;
    GUI_Label lbl_login, lbl_password, lbl_status;
    GUI_Edit edit_login, edit_password;
    GUI_Texture logo;

    public Login()
    {
        GUI.reCreate();

        logo = new GUI_Texture(GUI.rootNormal());
        logo.setTexture(Main.getAssetManager().get(Config.RESOURCE_DIR + "origin_logo.png", Texture.class));
        logo.SetPos(0, 10);
        logo.CenterX();

        int ypos = logo.pos.y + logo.Height() + 20;
        ypos = Math.max(ypos, (Gdx.graphics.getHeight() / 2) - 90);

        lbl_login = new GUI_Label(GUI.rootNormal());
        lbl_login.caption = Lang.getTranslate("login", "account");
        lbl_login.SetPos(10, ypos);
        lbl_login.UpdateSize();
        lbl_login.CenterX();

        edit_login = new GUI_Edit(GUI.rootNormal());
        edit_login.SetSize(150, 25);
        edit_login.SetPos(lbl_login.pos.add(0, 15));
        edit_login.CenterX();
        edit_login.text = Config.account;

        lbl_password = new GUI_Label(GUI.rootNormal());
        lbl_password.caption = Lang.getTranslate("login", "password");
        lbl_password.SetPos(edit_login.pos.add(0, 40));
        lbl_password.UpdateSize();
        lbl_password.CenterX();

        edit_password = new GUI_Edit(GUI.rootNormal());
        edit_password.SetSize(150, 25);
        edit_password.SetPos(lbl_password.pos.add(0, 15));
        edit_password.CenterX();
        edit_password.secret_symbol = "*";
        edit_password.allow_copy = false;
        edit_password.text = "123"; //Config.password;


        btn_login = new GUI_Button(GUI.rootNormal())
        {
            @Override
            public void DoClick()
            {
                doLogin();
            }
        };
        btn_login.caption = Lang.getTranslate("login", "login");
        btn_login.SetPos(edit_password.pos.add(0, 40));
        btn_login.SetSize(100, 25);
        btn_login.CenterX();

        lbl_status = new GUI_Label(GUI.rootNormal());
        lbl_status.caption = "";
        lbl_status.SetPos(btn_login.pos.add(0, 35));


        btn_exit = new GUI_Button(GUI.rootNormal())
        {
            @Override
            public void DoClick()
            {
                System.exit(0);
            }
        };
        btn_exit.caption = Lang.getTranslate("generic", "quit");
        btn_exit.SetSize(100, 25);
        btn_exit.SetPos(Gdx.graphics.getWidth() - 110, Gdx.graphics.getHeight() - 35);

        btn_options = new GUI_Button(GUI.rootNormal());
        btn_options.caption = Lang.getTranslate("generic", "options");
        btn_options.SetSize(100, 25);
        btn_options.SetPos(btn_exit.pos.add(0, -35));
        btn_options.enabled = false;

        if (!edit_login.text.isEmpty())
        {
            GUI.getInstance().SetFocus(edit_password);
            edit_password.SelectAll();
        }

        if (Config.quick_login_mode)
            doLogin();

        // тут запустим музыку в фоне
        if (!LwjglApplicationConfiguration.disableAudio)
        {

        }
    }

    protected void doLogin()
    {
        if (edit_login.text.isEmpty() || edit_password.text.isEmpty())
        {
            Config.quick_login_mode = false;
            return;
        }

        _account = edit_login.text;
        _password = edit_password.text;
        Config.account = edit_login.text;
        Config.password = edit_password.text;
        Config.quick_login_mode = false;
        Config.SaveOptions();
        _status = "login";

        btn_login.enabled = false;
        edit_login.enabled = false;
        edit_password.enabled = false;

        if (Net.getConnection() != null)
        {
            Net.getConnection().Close();
        }
        Net.NewConnection(Config.login_server, Config.login_server_port, NettyConnection.ConnectionType.LOGIN_SERVER);

    }

    static public void setStatus(String s)
    {
        _status = s;
    }

    static public void Error(String s)
    {
        _status = s;
        Main.ReleaseAll();
    }


    public void onUpdate()
    {
        // upd block
        if (Input.KeyHit(com.badlogic.gdx.Input.Keys.TAB))
        {
            if (!edit_login.isFocused())
                GUI.getInstance().SetFocus(edit_login);
            else if (!edit_password.isFocused())
                GUI.getInstance().SetFocus(edit_password);
        }

        if (Input.KeyHit(com.badlogic.gdx.Input.Keys.ENTER) && !Input.isAltPressed() && !Input.isCtrlPressed() && !Input
                .isShiftPressed())
        {
            doLogin();
        }

        if (!_status.isEmpty())
        {
            lbl_status.caption = Lang.getTranslate("net_error", _status);
            UpdateStatusLbl();
        }
        else if (Net.getConnection() != null)
        {
            lbl_status.caption = Lang.getTranslate("net_error", "connect_error");
            UpdateStatusLbl();
        }
        else
        {
            lbl_status.caption = Lang.getTranslate("net_error", "disconnected");
            UpdateStatusLbl();
        }
    }

    private void UpdateStatusLbl()
    {
        lbl_status.UpdateSize();
        lbl_status.CenterX();
    }

    public static void Show()
    {
        Main.freeScreen();
        Main.getInstance().setScreen(new Login());
    }
}
