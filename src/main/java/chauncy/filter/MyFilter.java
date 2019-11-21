package chauncy.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**   
 * @classDesc: 功能描述(使用Zuul实现服务过滤)  
 * @author: ChauncyWang
 * @createTime: 2019年11月21日 上午10:54:53   
 * @version: 1.0  
 */  
@Component
public class MyFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(MyFilter.class);
	
	/**
	 * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下： 
	 * 1.pre：路由之前。
	 * 2.routing：路由之时。
	 * 3.post： 路由之后。
	 * 4.error：发送错误调用。
	 */
	@Override
	public String filterType() {
		return "pre";
	}
	
	/**
	 * filterOrder：过滤的顺序。
	 */
	@Override
	public int filterOrder() {
		return 0;
	}
	
	/**
	 * shouldFilter：这里可以写逻辑判断，是否要过滤。代码采用return true;表永远过滤。
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	/**
	 * run：过滤器的具体逻辑。可用很复杂，包括查sql、nosql去判断该请求到底有没有权限访问。
	 */
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
		Object accessToken = request.getParameter("token");
		if (accessToken == null) {
			log.warn("token is empty");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			try {
				ctx.getResponse().getWriter().write("token is empty");
			} catch (Exception e) {
			}

			return null;
		}
		log.info("ok");
		return null;
	}
}
