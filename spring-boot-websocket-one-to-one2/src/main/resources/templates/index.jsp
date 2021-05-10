<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
    <title>Hello WebSocket</title>
    <link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/sockjs-client/1.4.0/sockjs.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <script src="app-websocket.js"></script>
</head>
<body>
    <div id="main-content" class="container" style="margin-top: 10px;">
        <div class="row">
        	<table class="people_list">
<!--         		<tr> -->
<!--         			<td>hello</td> -->
<!--         		</tr> -->
<!--         		<tr> -->
<!--         			<td>hello</td> -->
<!--         		</tr>				 -->
<!--         		<tr> -->
<!--         			<td>hello</td> -->
<!--         		</tr>             	 -->
        	</table>
        </div>    
        <div class="row">
            <form class="navbar-form" style="margin-left:0px">
                <div class="col-md-12">
                    <div class="form-group">
                        <label>WebSocket 连接：</label>
                        <button class="btn btn-primary" type="button" onclick="connect();">进行连接</button>
                        <button class="btn btn-danger" type="button" onclick="disconnect();">断开连接</button>
                    </div>
                    <label>订阅地址：</label>
                    <div class="form-group">
                        <input type="text" id="subscribe" class="form-control" placeholder="订阅地址">
                    </div>
                    <button class="btn btn-warning" onclick="subscribeSocket();" type="button">订阅</button>
                </div>
            </form>
        </div>
        </br>
        <div class="row">
            <div class="form-group">
                <label>发送的目标用户：</label>
                <input type="text" id="targetUser" class="form-control" placeholder="发送的用户">
                <label for="content">发送的消息内容：</label>
                <input type="text" id="content" class="form-control" placeholder="消息的内容">
            </div>
            <button class="btn btn-info" onclick="sendMessageNoParameter();" type="button">发送</button>
        </div>
        </br>
        <div class="row">
            <div class="col-md-12">
                <h5 class="page-header" style="font-weight:bold">接收到的消息：</h5>
                <table class="table table-striped">
                    <tbody id="information"></tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>