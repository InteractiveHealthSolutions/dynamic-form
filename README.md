This is a spring and hibernate application so make sure that you have added all spring and hibnerate dependencies to your project.

- Add jar files in your WEB-INF/lib directory.
- Add fm folder in your webcontent or war folder 
- Add <context:component-scan base-package="org.ihs.form.controller" /> to your dispatcher servlet
- This application resolves /fm/jsp internally to .jsp so add these lines to your dispatcher servlet:

	<mvc:annotation-driven></mvc:annotation-driven>

	<bean
		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/fm/jsp/"></property>
		<property name="suffix" value=".jsp"></property>
	</bean>
	
- Database should be imported from init script in distribution folder.