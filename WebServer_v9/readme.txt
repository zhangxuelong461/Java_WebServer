重构响应

此版本我们首先在index.html页面上加入一个图片标签，发现无法正常显示
原因在于页面上有其他素材时浏览器会发送多次请求来下载这些资源并显示

因此，我们的服务端需要支持多次请求与响应。

按照HTTP1.0协议要求时，要求客户端与服务端建立TCP连接后允许一次请求与响应的交互后就要断开连接
因此我们首先修改ClientHandler,在处理完毕的最后finally中调用socket.close()方法与客户端断开TCP连接

然后在WebServer主类的start方法中将等待客户端的连接并进行处理的操作以while死循环形式重复接受此时
页面图片就可以正确显示了

2.导入学子商城资源后，访问首页时会出现无法正常显示的情况，跟踪请求发现所有资源都已经响应回来了，
究其原因是服务端在响应资源时，指定的响应头是Content-Type是写死的，固定发送text/html,
这导致浏览器不能正确理解其请求的资源从而出现显示不正确的情况，因此我们要将响应头发送的代码进行改造。
最终要根据实际请求的资源类型响应对应的Content-Type的值，并且也要做到发送响应头是可以设置的，而不是固定的
只发送Content-Type与Content-Length


此版本解决可以根据设置的响应头进行发送。
在HttpResponse中添加一个map属性，用来保存所有要发送给客户端的响应头，并且在sendHeaders方法中遍历这个
Map将所有响应头发送。

