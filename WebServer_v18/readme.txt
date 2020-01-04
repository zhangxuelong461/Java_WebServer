重构Servlet

用于处理业务的Servlet我们应当提取一个超类
一是规定一下所有的Servlet处理业务的方法统一，二是将重复代码进行复用

实现：
	1.在com.webserver.servlet包中新建类:HttpServlet，该类定义为抽象类，并定义抽象方法service
		这样所有Servlet实现它是就必须重写service方法
		
	2.让所有的Servlet都继承该类，并将重复代码提取到HttpServlet并进行复用(这里以跳转页面为例)
	

为了将来添加新的业务不再修改ClientHandler，因此我们要利用反射机制
	1.首先在com.webserver.core包中新建类：ServerContext，在这里维护一个Map
		key是请求路径，value为对应处理该请求的业务类(某Servlet)
	
	2.在conf目录下新建一个配置文件servelets.xml，在里面配置所有业务请求与对应的Servlet
	
	3.在ServerContext中初始化Map时通过解析xml文件并利用反射实例化对应的Servlet作为value,请求路径为key
	
	4.提供一个静态方法，根据请求路径获取对应的处理类(某Servlet)
	
	5.将ClientHandlet处理请求环节中原有的若干分支处理不同业务改为根据请求去ServerContext获取对应
		的处理类并调用其service方法处理即可。
		
	这样一来，将来添加新业务无需修改ClientHandler，只需要在servlets.xml配置即可
	
	
	
	