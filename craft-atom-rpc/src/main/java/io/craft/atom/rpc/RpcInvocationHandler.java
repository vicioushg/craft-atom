package io.craft.atom.rpc;

import io.craft.atom.protocol.rpc.model.RpcMessage;
import io.craft.atom.rpc.spi.RpcInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mindwind
 * @version 1.0, Aug 20, 2014
 */
public class RpcInvocationHandler implements InvocationHandler {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(RpcInvocationHandler.class);
	
	
	@Getter @Setter private RpcInvoker invoker;
	
	
	public RpcInvocationHandler(RpcInvoker invoker) {
		this.invoker = invoker;
	}
	

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?>   rpcInterface   = method.getDeclaringClass();
		String     methodName     = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();
		Object[]   parameters     = args;
		RpcMessage req = RpcMessages.newRequestRpcMessage(rpcInterface, methodName, parameterTypes, parameters);
		
		LOG.debug("[CRAFT-ATOM-RPC] Rpc client proxy before invocation, |req={}|", req);
		RpcMessage rsp = invoker.invoke(req);
		LOG.debug("[CRAFT-ATOM-RPC] Rpc client proxy after  invocation, |rsp={}|", rsp);
		
		return RpcMessages.unpackResponseMessage(rsp);
	}

}
