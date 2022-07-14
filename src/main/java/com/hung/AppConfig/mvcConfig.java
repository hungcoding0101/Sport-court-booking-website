package com.hung.AppConfig;

import java.util.HashSet;

import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.descriptor.LocalResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import com.hung.CustomConverter.StringToDateConverter;
import com.hung.CustomConverter.StringToTimeConverter;
import com.hung.CustomConverter.StringArrayToTimeConverter;
import com.hung.CustomValidator.CombineJSR303AndSpringValidation;
import com.hung.CustomValidator.NameDuplicationSpringValidation;
import com.hung.Interceptor.CheckLogInInterceptor;
import com.hung.Interceptor.Interceptor;

@Configuration
@ComponentScan(basePackages = {"com.hung.Controllers","com.hung.CustomValidator"})
@EnableAsync
@EnableCaching 
public class mvcConfig extends WebMvcConfigurationSupport {
	
	@Override // Enable use of PathPattern
	  public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setPatternParser(new PathPatternParser());
	}
	

//	@Override
//	protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//		configurer.setDefaultTimeout(10000);
//		configurer.setTaskExecutor(threadPoolTaskExecutor());
//	}
	
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	
	@Bean
		public HandlerInterceptor interceptor() {
			return new Interceptor();
		}
	
	@Bean
		public CheckLogInInterceptor checkLogInInterceptor() {
			return new CheckLogInInterceptor();
	}
	
	@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(interceptor());	
			LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
			localeChangeInterceptor.setParamName("language");
			registry.addInterceptor(localeChangeInterceptor);			
		}

	
//	@Bean
//		public LocaleResolver localeResolver() {
//			SessionLocaleResolver resolver = new SessionLocaleResolver(); 
//			resolver.setDefaultLocale(new Locale("en"));
//			return resolver;
//		}
	
	@Bean
		public MessageSource messageSource() {
			ResourceBundleMessageSource resource = new ResourceBundleMessageSource();
			resource.setBasename("messages"); 
			return resource;
		}
		
	
	@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
					registry.addResourceHandler("/resources/**").addResourceLocations("WEB-INF/resources/")
						.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)).resourceChain(false)
						.addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
					registry.addResourceHandler("/Config/**").addResourceLocations("file:///D:/Config/");
		}
	
	
		//This filter manages versions of static resources
	@Bean
		  public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
		    return new ResourceUrlEncodingFilter();
		  }
	
	@Bean
		public EhCacheManagerFactoryBean ehCacheManagerFactory() {
			 	EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
		        cacheManagerFactoryBean.setShared(true);
	
		        return cacheManagerFactoryBean;
		}
	
	@Bean
		public EhCacheCacheManager ehCacheCacheManager(){
			EhCacheCacheManager cacheManager = new EhCacheCacheManager();
			cacheManager.setCacheManager(ehCacheManagerFactory().getObject());
			cacheManager.setTransactionAware(true);
			return cacheManager;
	}
	
	@Bean
		public LocalValidatorFactoryBean validator() {
			LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
			bean.setValidationMessageSource(messageSource());
			return bean; 
		}
	
	@Bean
		public MethodValidationPostProcessor methodValidationPostProcessor() {
			return new MethodValidationPostProcessor();
		}
	
	@Override
	public Validator getValidator() {
		return validator();
	}
	
	@Bean 
	public CommonsMultipartResolver multipartResolver() {
		 CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		 resolver.setDefaultEncoding("utf-8");
		 return resolver;
	}

	@Override
		public void addFormatters(FormatterRegistry registry) {
			registry.addConverter(new StringToDateConverter());
			registry.addConverter(new StringArrayToTimeConverter());
			registry.addConverter(new StringToTimeConverter());
		}
	
	@Override
		protected void addViewControllers(ViewControllerRegistry registry) {
//			registry.addViewController("/").setViewName("home");	
			registry.addViewController("/home").setViewName("home");
		}
	
	
	@Bean
	public CombineJSR303AndSpringValidation combineJSR303AndSpringValidation(@Autowired NameDuplicationSpringValidation nameDuplicationSpringValidation) {
		Set<Validator> springValidators = new HashSet<>();
		springValidators.add(nameDuplicationSpringValidation);
		CombineJSR303AndSpringValidation combiner = new CombineJSR303AndSpringValidation();
		combiner.setSpringValidators(springValidators);
		return combiner;
	}
	
}
