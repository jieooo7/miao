/**  
 * Copyright © 2015 The Code. All rights reserved.
 *
 * @Title: UrlConfig.java
 * @Prject: Tour
 * @Package: com.tutwo.tour.config
 * @Description: TODO
 * @author: Andrew Lee  
 * @date: 2015-1-12 下午2:47:33
 * @version: V1.0  
 */
package com.liuzhuni.lzn.config;

/**
 * @ClassName: UrlConfig
 * @Description: TODO
 * @author: Andrew Lee
 * @date: 2015-1-12 下午2:47:33
 */

public class UrlConfig {
	
	public static final String BASE_IP = "http://192.168.1.110:8058/api/";
//	public static final String BASE_IP = "http://hmapp.liuzhuni.com/api/";
	public static final String VERSION_CODE = "5";
	public static final String INDEX = BASE_IP +"usershoplist/get?id=";
	public static final String SEND_CODE = BASE_IP +"reg/sentcode";
	public static final String SEND_FOGOT_CODE = BASE_IP +"forgot/sentcode";
	public static final String CHECK_CODE = BASE_IP +"reg/checkcode";
	public static final String REG_PASSWD_SET = BASE_IP +"reg/reg";
	public static final String USER_SIGN = BASE_IP +"user/sign";
	public static final String GET_MESSAGE_COUNT = BASE_IP +"user/GetMesCount";
	public static final String SEX_SET = BASE_IP +"user/putgender";
	public static final String FEED_BACK = BASE_IP +"other/PostSuggent";
	public static final String LOGOUT = BASE_IP +"user/logout";
	public static final String LOGIN = BASE_IP +"login/Get";
	public static final String NAME_SET = BASE_IP +"user/put";
	public static final String PUSH_SET = BASE_IP +"User/PutPush";
	public static final String MESSAGE_CENTER = BASE_IP +"user/getmessage/";
	public static final String WANT_BUY = BASE_IP +"user/GetShopList/";
	public static final String DELETE_BUY_ITEM = BASE_IP +"user/DeleteShopList/";
	public static final String GET_BRAND = BASE_IP +"usershoplist/getbrand/";
	public static final String SCAN = BASE_IP +"barcode/scan";
	public static final String SCAN_DETAIL = BASE_IP +"barcode/Detail";
	public static final String POST_ADDRES = BASE_IP +"user/PostAddr";
	public static final String SELECT_SUBMIT = BASE_IP +"usershoplist/Wantbuy";
	public static final String BUY_FEED = BASE_IP +"usershoplist/Satisfied";
	public static final String IMG_UPLOAD = BASE_IP +"user/Avatar";
	public static final String BIND_TEL = BASE_IP +"user/sentcode";
	public static final String BIND_TEL_SUB = BASE_IP +"user/checkcode";
	public static final String DROP_NOTI = BASE_IP +"user/DropPrice";
	public static final String PUSH_CLIENT_ID = BASE_IP +"user/GeTuiClientID";
	public static final String FORGOT_CHECK_CODE = BASE_IP +"forgot/checkcode";
	public static final String FORGOT_PASSWD_SET = BASE_IP +"forgot/SetPassword";
	public static final String THIRD_LOGIN = BASE_IP +"reg/RegPlatform";
	public static final String GET_SHARE = BASE_IP +"other/GetShare";
	public static final String GET_HISTORY = BASE_IP +"user/dialog?id=";
	public static final String GET_DIALOG = BASE_IP +"dialog/GetDialog?id=";
	public static final String GET_PRICE = BASE_IP +"usershoplist/getprice";




	public static final String GET_PICK = BASE_IP +"product/getcontent";
	public static final String GET_NEWS = BASE_IP +"product/getbrokecontent";
	public static final String GET_FILTER= BASE_IP +"product/gettags";
	public static final String GET_SELECT_DETAIL= BASE_IP +"product/getcontent?id=";
	public static final String GET_NEWS_DETAIL=BASE_IP +"Product/GetBrokeContent?id=";
	public static final String COMMENT_SEL=BASE_IP +"Product/GetReview";
	public static final String COMMENT_NEWS=BASE_IP +"Product/GetBrokeReview";
	public static final String COMMENT_SEL_REPLY=BASE_IP +"Product/AddReview";
	public static final String COMMENT_NEWS_REPLY=BASE_IP +"Product/AddBrokeReview";




	public static final String GET_PROFILE=BASE_IP +"user/get";
	public static final String FAV=BASE_IP +"product/CollectionNum";
	public static final String CANCEL_FAV=BASE_IP +"product/CancelCollect";
	public static final String GET_TOP=BASE_IP +"product/GetProductTop";
	public static final String GET_CHEAP=BASE_IP +"product/GetBaicai";
	public static final String PRICE_RANGE=BASE_IP +"usershoplist/GetPriceRange";




	public static final String SEARCH_PICK=BASE_IP +"product/SearchContent";
	public static final String SEARCH_NEWS=BASE_IP +"product/SearchBrokeContent";



	public static final String CAMPAIGN=BASE_IP +"user/GetZhuanTiList";


	public static final String IS_SHARE=BASE_IP +"user/IsShare";
	public static final String SUCCESS_SHARE=BASE_IP +"user/successshare";
	public static final String GET_COUPON=BASE_IP +"user/getcoupon";

	public static final String GET_HTML=BASE_IP +"other/transit";







}
