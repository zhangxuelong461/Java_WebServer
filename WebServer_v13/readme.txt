重构响应

本版本我们支持所有的介质类型。上一个版本中我们对于资源对应的Content-Type我们仅支持了6个。
这里我们引用Tomcat整理的所有类型(在tomcat安装目录下的con目录下有一个web.xml文件)来初始化，使得我们
也能支持这1000多个类型。

实现：
	1.在项目目录下新建一个目录conf
	2.将web.xml文件拷贝到conf目录中
	3.修改HttpContext初始化mimeMapping这个Map的方法，改为通过解析web.xml文件初始化
	
	