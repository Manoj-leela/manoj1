package controllers;

import java.io.IOException;

import platform.Platform;

import com.squareup.okhttp.ResponseBody;

import http.APIResponse;

public class TestSMS {

	public static void main(String[] args) throws IOException {

		
		
		Platform p = new Platform("5C15396Df3e3d4897A2419e4343cf4363B3Febf780F90e01226d87A2419F4DD6", "2005E1EF9c8dBB7C7CE88526d9106A0391E4c0ff0099DA99C21Bd384cea64E5A", Platform.Server.PRODUCTION);

		p.authorize("18556992400", "102 ", "Syml@123");

		System.out.println("Access Token: "+p.getAccessToken());

		APIResponse obj = p.apiCall("GET", "/restapi/v1.0/account/~/extension/~/sms",null,null);

		ResponseBody str2 = obj.body();

		System.out.println(str2.string());

		//save(str2.string());

		System.out.println("done 2");

		System.out.println(obj.error());
	}

}
