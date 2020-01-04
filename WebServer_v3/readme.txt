本版本继续完成解析请求的工作

HTTP协议中一个请求由三部分构成，因此我们设计一个类HttpRequest，将请求的各部分内容作为属性定义出
来。解析请求时，我们将客户端发送的请求最终以一个HttpRequest的实例表示出来，以便后续处理请求时
可以通过该对象获取请求中各部分内容

实现：
	1.新建一个包:com.webserver.http
		这个包存放所有有关http协议的内容
	
	2.在http包中新建类：HttpRequest 请求对象
	
	3.在ClientHandler中处理流程中完成第一步：
		解析请求，该工作实际就是实例化HttpRequest具体的解析工作交给HttpRequest自行完成。
	
	4.完成HttpRequest构造方法，在其中解析请求并将请求各信息设置到对应属性上。

