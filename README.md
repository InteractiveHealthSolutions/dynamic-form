This is a spring and hibernate application so make sure that you have added all spring and hibnerate dependencies to your project.

- Add jar (which ones? form where?) files in your WEB-INF/lib directory.
- Paste dfmv folder in your webcontent or war folder 
- Copy form-servlet.xml to your WEB-INF
- Add following to your web.xml
```
	 <servlet>
    	<servlet-name>form</servlet-name>
      	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
      	<load-on-startup>1</load-on-startup>
  	</servlet>
  	<servlet-mapping>
      	<servlet-name>form</servlet-name>
      	<url-pattern>/dfm/*</url-pattern>
  	</servlet-mapping>
```

- Database should be imported from init script in distribution folder(from where ??)
- Form Module can be accessed on /dfm/ URL (see here??? for available ajax/html) 
- User manual can be accessed here??