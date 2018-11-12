package com.acq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.acq.web.dto.impl.ServiceDto;



 
public class AcqSession {
	public static ServiceDto<String> isCustSessionRunning(HttpServletRequest request,String custSessionId){
		  ServiceDto response = new ServiceDto();
		  try {
			  HttpSession session = request.getSession();
		      if ((String) session.getAttribute("ambiguityerrercode") == null) {
		    	  response.setMessage(EnumStatusConstant.InvalidSession.getDescription());
		    	  response.setStatus(EnumStatusConstant.InvalidSession.getId());
		      }else{
		    	  String custSessionIdd = (String) session.getAttribute("ambiguityerrercode");
		    	  //System.out.println("ambiguityerrercode:"+(String) session.getAttribute("ambiguityerrercode"));
		    	  //System.out.println("custSessionId:"+custSessionId);
		    	  //System.out.println("custSessionIdd:"+custSessionIdd);
		    	  if (custSessionId.equals(custSessionIdd)) {
		    		  //System.out.println("kkkkkkkkkkkkkkkkkkkk");
		    		  //response.setMessage(EnumStatusConstant.OK.getDescription());
		    		  response.setStatus(EnumStatusConstant.OK.getId());
		    	  }else{
		    		 // System.out.println("pppppppppppppppppppppppp");
		    		  response.setMessage(EnumStatusConstant.InvalidSession.getDescription());
		    		  response.setStatus(EnumStatusConstant.InvalidSession.getId());
		    	  }
		      }
		 }catch (Exception e) {
			  System.out.println("error is generating in check session............. ");
			  response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			  response.setStatus(EnumStatusConstant.RollBackError.getId());
		 }return response;
	}	
	
	public static ServiceDto<String> isSessionRunning(HttpServletRequest request,String userId){
		ServiceDto response = new ServiceDto();
		try {
			HttpSession session = request.getSession();
			//System.out.println("session obj is created");
			//System.out.println("usename:"+ (String) session.getAttribute("uname"));
			//System.out.println("userId:"+(String) session.getAttribute("userid"));
				if ((String) session.getAttribute("userid") == null) {
				response.setMessage(EnumStatusConstant.InvalidSession.getDescription());
				response.setStatus(EnumStatusConstant.InvalidSession.getId());
			} else {						
				//System.out.println("usename1:"+ (String) session.getAttribute("uname"));
				//System.out.println("userId1:"+(String) session.getAttribute("userid"));
				String userIdd = (String) session.getAttribute("userid");
				if (userId.equals(userIdd)) {
					response.setMessage(EnumStatusConstant.OK.getDescription());
					response.setStatus(EnumStatusConstant.OK.getId());
				}else{
					response.setMessage(EnumStatusConstant.InvalidSession.getDescription());
					response.setStatus(EnumStatusConstant.InvalidSession.getId());
				}
			}
		} catch (Exception e) {
			System.out.println("error is generating............. ");
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setStatus(EnumStatusConstant.RollBackError.getId());
		}
		return response;
	}


}
