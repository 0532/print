<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
    <META http-equiv=Content-Type content="text/html; charset=gb2312">
    <LINK href="css/admin.css" type="text/css" rel="stylesheet">
</HEAD>
<BODY>
<TABLE cellSpacing=0 cellPadding=0 width="100%" align=center border=0>
    <TR height=28>
        <TD background=images/title_bg1.jpg>当前位置: 管理主页</TD>
    </TR>
    <TR>
        <TD bgColor=#b1ceef height=1></TD>
    </TR>
    <TR height=20>
        <TD background=images/shadow_bg.jpg></TD>
    </TR>
</TABLE>
<TABLE cellSpacing=0 cellPadding=0 width="90%" align=center border=0>
    <TR height=100>
        <TD align=middle width=100><IMG height=100 src="images/admin_p.gif"
                                        width=90></TD>
        <TD width=60>&nbsp;</TD>
        <TD>
            <TABLE height=100 cellSpacing=0 cellPadding=0 width="100%" border=0>

                <TR>
                    <TD>当前时间：<%=new Date().toLocaleString()%>
                    </TD>
                </TR>
                <TR>
                    <TD style="FONT-WEIGHT: bold; FONT-SIZE: 16px">admin</TD>
                </TR>
                <TR>
                    <TD>欢迎进入网站管理中心！</TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
    <TR>
        <TD colSpan=3 height=10></TD>
    </TR>
</TABLE>
<TABLE cellSpacing=0 cellPadding=0 width="95%" align=center border=0>
    <TR height=20>
        <TD></TD>
    </TR>
    <TR height=22>
        <TD style="PADDING-LEFT: 20px; FONT-WEIGHT: bold; COLOR: #ffffff"
            align=middle background=images/title_bg2.jpg>您的相关信息
        </TD>
    </TR>
    <TR bgColor=#ecf4fc height=12>
        <TD></TD>
    </TR>
    <TR height=20>
        <TD></TD>
    </TR>
</TABLE>
<TABLE cellSpacing=0 cellPadding=2 width="95%" align=center border=0>
    <TR>
        <TD align=right width=100>登陆帐号：</TD>
        <TD style="COLOR: #880000">admin</TD>
    </TR>
    <TR>
        <TD align=right>真实姓名：</TD>
        <TD style="COLOR: #880000">admin</TD>
    </TR>
    <TR>
        <TD align=right>注册时间：</TD>
        <TD style="COLOR: #880000">2014-04-17 15:02:04</TD>
    </TR>
    <TR>
        <TD align=right>登陆次数：</TD>
        <TD style="COLOR: #880000">58</TD>
    </TR>
    <TR>
        <TD align=right>上线时间：</TD>
        <TD style="COLOR: #880000">2014-04-18 15:02:04</TD>
    </TR>
    <TR>
        <TD align=right>IP地址：</TD>
        <TD style="COLOR: #880000">127.0.0.1</TD>
    </TR>
    <TR>
        <TD align=right>身份过期：</TD>
        <TD style="COLOR: #880000">30 分钟</TD>
    </TR>
    <TR>
        <TD align=right>网站开发QQ：</TD>
        <TD style="COLOR: #880000">529726271</TD>
    </TR>
</TABLE>
</BODY>
</HTML>