package com.mrmulti.Util;

import com.mrmulti.Api.Request.ChoosePaymentGatewayRequest;
import com.mrmulti.Api.Request.GatewayTransactionRequest;
import com.mrmulti.Api.Request.PayTMTransactionUpdateRequest;
import com.mrmulti.Api.Response.BasicResponse;
import com.mrmulti.Api.Response.PaymentChooseResponse;
import com.mrmulti.Api.Response.PaymentTransactionResponsedto.PaymentChooseResponse.PaymentTransactionResponse;
import com.mrmulti.BalanceCheck.dto.BalanceCheckResponse;
import com.mrmulti.BrowsePlan.dto.ResponsePlan;
import com.mrmulti.CommissionSlab.dto.CommissionSlabResponse;
import com.mrmulti.DMR.dto.LoginSenderResponse;
import com.mrmulti.DMRReport.dto.DMRReportResponse;
import com.mrmulti.DTHReversal.dto.ChatResponse;
import com.mrmulti.DTHReversal.dto.DTHTicketResponse;
import com.mrmulti.DTHReversal.dto.ReversalOpenListResponse;
import com.mrmulti.Dashboard.dto.RechargeInfoResponse;
import com.mrmulti.Dashboard.ui.DownlineUserResponse;
import com.mrmulti.Dashboard.ui.UserListReportResponse;
import com.mrmulti.DisputeReport.dto.DisputeResponse;
import com.mrmulti.DthBook.dto.GetDthBookingResponse;
import com.mrmulti.FundRecReport.dto.FundRecResponse;
import com.mrmulti.GiftCardServices.dto.GiftCardServicesResponse;
import com.mrmulti.LedgerReport.dto.LedgerReportResponse;
import com.mrmulti.Login.dto.GetOperatorResponse;
import com.mrmulti.Login.dto.LoginResponse;
import com.mrmulti.ROffer.dto.ROfferResponse;
import com.mrmulti.RechargeReport.dto.RechargeReportResponse;
import com.mrmulti.Register.dto.RegisterResponse;
import com.mrmulti.UserDayBook.dto.UserDayBookResponse;
import com.mrmulti.Util.dto.BankDetailResponse;
import com.mrmulti.Util.dto.BankListResponse;
import com.mrmulti.Util.dto.HelloWorldResponse;
import com.mrmulti.Util.dto.KeyUpdateResponse;
import com.mrmulti.Util.dto.NumberListResponse;
import com.mrmulti.Util.dto.RechargeResponse;
import com.mrmulti.Util.dto.ServicesResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Lalit on 20-01-2017.
 */

public interface EndPointInterface {

    @GET("/webservice/Androidapp.asmx/HelloWorld")
    Call<HelloWorldResponse> helloWorld();

    @GET("/webservice/Androidapp.asmx/VideoImageGallery")
    Call<LoginResponse> VideoImageGallery(@Query("_AppId") String _AppId,
                                          @Query("_RoleId") String _RoleId);

    @GET("/webservice/Androidapp.asmx/OfferSheet")
    Call<LoginResponse> OfferSheet(@Query("_AppId") String _AppId);

    @GET("/webservice/Androidapp.asmx/ForgotPassword?")
    Call<LoginResponse> forgotPassword(@Query("_UMobile") String uMobile);

    @Headers("Content-Type: application/json")
    @POST("/PGCallbackResponse/RazorpayApp?")
    Call<BasicResponse> RazorpayUpdate(@Body RazorpayresponseUpdate request);

    @GET("/Rechargeplan.asmx/RechargePlans?")
    Call<ResponsePlan> RechargePlans(@Query("OperatorId") String OperatorId,
                                     @Query("ZoneId") String ZoneId,
                                     @Query("Token") String Token);

    @GET("/webservice/Androidapp.asmx/App_Services_Provided")
    Call<ServicesResponse> App_Services_Provided(@Query("_AppId") String appInfo);

    @GET("/webservice/Androidapp.asmx/GetOperators")
    Call<GetOperatorResponse> GetOperators(@Query("_AppId") String appInfo);

    @GET("/webservice/Androidapp.asmx/GetNotice")
    Call<GetOperatorResponse> GetNotice(@Query("_AppId") String appInfo);

    @GET("/webservice/Androidapp.asmx/VideoImageGallery?")
    Call<LoginResponse> banner(@Query("_AppId") String _AppId,
                               @Query("_RoleId") String _RoleId);
//  @GET("/API/B2BSecureService.asmx/VideoImageGallery?")
//    Call<LoginResponse> banner(@Query("_AppId") String _AppId,
//                               @Query("_RoleId") String _RoleId);


@GET("/webservice/Androidapp.asmx/BannerSetting?")
    Call<LoginResponse> BannerSetting(@Query("_AppId") String _UMobile
                                       , @Query("_Roleid") String _RoleId);

    @GET("/webservice/Androidapp.asmx/GetSaleCoupan")
    Call<GetOperatorResponse> GetSaleCoupan(@Query("_AppId") String appInfo);

    @GET("/webservice/Androidapp.asmx/GetMyCoupan")
    Call<GetOperatorResponse>GetmyCoupan(@Query("_UMobile") String appInfo,
                                         @Query("_UPassword") String name);


    @GET("/webservice/Androidapp.asmx/GetDTHConnectionOperators")
    Call<GetDthBookingResponse> GetConnectionOperators(@Query("_AppId") String appInfo);


    @GET("/webservice/Androidapp.asmx/GetNumberList?")
    Call<NumberListResponse> GetNumberList(@Query("_AppInfo") String appInfo);

    @GET("/webservice/Androidapp.asmx/BankDetail?")
    Call<BankDetailResponse> BankDetail(@Query("_UMobile") String appInfo,
                                        @Query("_UPassword") String name);

    @GET("/webservice/Androidapp.asmx/Logins?")
    Call<LoginResponse> Logins(@Query("_UMobile") String appInfo,
                                    @Query("_UPassword") String name,
                                    @Query("_DeviceId") String deviceId,
                                    @Query("_FCMI") String _FCMI,
                                    @Query("_ReffId") String reffId,
                                    @Query("_OTP") String otp);

    @GET("/webservice/Androidapp.asmx/Login?")
    Call<LoginResponse> secureLogin(@Query("_UMobile") String appInfo,
                                    @Query("_UPassword") String name,
                                    @Query("_DeviceId") String deviceId
                                    /*@Query("_ReffId") String reffId,
                                    @Query("_OTP") String otp*/);

    @GET("/webservice/Androidapp.asmx/BookDTHConnection?")
    Call<GetDthBookingResponse> BookDTHConnection(@Query("_AppId") String appInfo,
                                                  @Query("userid") String userid,
                                                  @Query("dthid") String dthid,
                                                  @Query("PackagelanguageName") String PackagelanguageName,
                                                  @Query("packNAme") String packNAme,
                                                  @Query("packageName") String packageName,
                                                  @Query("Amount") String Amount,
                                                  @Query("customerName") String customerName,
                                                  @Query("HNONumber") String HNONumber,
                                                  @Query("landmark") String landmark,
                                                  @Query("address") String address,
                                                  @Query("mobileno1") String mobileno1,
                                                  @Query("mobileno2") String mobileno2,
                                                  @Query("pincode") String pincode,
                                                  @Query("packlanguagename") String packlanguagename);

    @GET("/webservice/Androidapp.asmx/UserList?")
    Call<UserListReportResponse> UserListStatus(@Query("UMobile") String appInfo,
                                                @Query("UPassword") String password,
                                                @Query("_Search") String search);
    @GET("/webservice/Androidapp.asmx/GetDownlineRole?")
    Call<UserListReportResponse> UserRole(@Query("_UMobile") String appInfo,
                                                @Query("_UPassword") String password,
                                                @Query("loginid") String loginid);
    @GET("/webservice/Androidapp.asmx/GetDownlineUsersByRole?")
    Call<DownlineUserResponse> GetDownlineUsersByRole(@Query("_UMobile") String appInfo,
                                            @Query("_UPassword") String password,
                                            @Query("roleid") String roleid,
                                            @Query("loginid") String loginid);
 @GET("/webservice/Androidapp.asmx/GetDownlineUser?")
    Call<DownlineUserResponse> DownlineUser(@Query("_UMobile") String appInfo,
                                            @Query("_UPassword") String password,
                                            @Query("uid") String uid );



    @GET("/webservice/B2BSecureService.asmx/CheckDTHPlan?")
    Call<ResponseMobilePlan> CheckDTHPlan(@Query("MobileNo") String MobileNo,
                                          @Query("RofferCode") String RofferCode);

    @GET("/webservice/B2BSecureService.asmx/CheckMobSimplePlan?")
    Call<ResponseMobilePlan> CheckMobSimplePlan(@Query("OPID") String MobileNo,
                                                @Query("Circle") String RofferCode);
    @GET("/webservice/Androidapp.asmx/DownlineUserStockNotTaken")
    Call<DownlineUserResponse> DownlineUserStockNotTaken(@Query("_UMobile") String _UMobile,
                                                     @Query("_UPassword") String _UPassword,
                                                     @Query("MemberId") String MemberId);

    @GET("/webservice/Androidapp.asmx/DirectRetailerAndTeamRetailerBusiness")
    Call<DownlineUserResponse> BusinessList(@Query("_UMobile") String _UMobile,
                                                     @Query("_UPassword") String _UPassword,
                                                     @Query("MemberId") String MemberId);
    @GET("/webservice/Androidapp.asmx/DownlineUserRechargeNotDone")
    Call<DownlineUserResponse> DownlineUserRechargeNotDone(@Query("_UMobile") String _UMobile,
                                                       @Query("_UPassword") String _UPassword,
                                                       @Query("MemberId") String MemberId);
    @GET("/webservice/Androidapp.asmx/LookUpApi?")
    Call<GetOperatorResponse> LookUpApi(@Query("_UMobile") String _UMobile,
                                        @Query("_UPassword") String _UPassword,
                                        @Query("_Mobile") String _Mobile);

    @GET("/WebService/Instantpay.asmx/GetGiftVoucher?")
    Call<GiftCardServicesResponse> GetGiftVoucher();

    @GET("/webservice/Androidapp.asmx/GiftCardRecharge?")
    Call<GiftCardServicesResponse> GetGiftVoucherRecharge(@Query("_UMobile") String _UMobile,
                                                          @Query("_UPassword") String _UPassword,
                                                          @Query("Code") String Code,
                                                          @Query("gv_amt") String gv_amt,
                                                          @Query("gv_sndr_name") String gv_sndr_name,
                                                          @Query("gv_sndr_email") String gv_sndr_email,
                                                          @Query("gv_recv_name") String gv_recv_name,
                                                          @Query("gv_recv_email") String gv_recv_email,
                                                          @Query("gv_sndr_address") String gv_sndr_address,
                                                          @Query("gv_sndr_pincode") String gv_sndr_pincode,
                                                          @Query("gv_sndr_city") String gv_sndr_city,
                                                          @Query("gv_sndr_state") String gv_sndr_state,
                                                          @Query("gv_sndr_mobile") String gv_sndr_mobile,
                                                          @Query("gv_recv_address") String gv_recv_address,
                                                          @Query("gv_recv_pincode") String gv_recv_pincode,
                                                          @Query("gv_recv_city") String gv_recv_city,
                                                          @Query("gv_recv_state") String gv_recv_state,
                                                          @Query("gv_recv_mobile") String gv_recv_mobile,
                                                          @Query("gv_gift_msg") String gv_gift_msg);


    @GET("/webservice/Androidapp.asmx/GetDTHConnectionAmount?")
    Call<GetDthBookingResponse> GetDTHConnectionAmount(@Query("_AppId") String appInfo,
                                                       @Query("dthid") String dthid,
                                                       @Query("languageid") String languageid,
                                                       @Query("packid") String packid,
                                                       @Query("packageid") String packageid);

    @GET("/webservice/Androidapp.asmx/GetFlashNews?")
    Call<BankDetailResponse> GetNews(@Query("_AppId") String appInfo,@Query("_RoleId") String _RoleId);


    @GET("/webservice/Androidapp.asmx/UserSaleReport?")
    Call<GetOperatorResponse> UserSaleReport(@Query("_UMobile") String appInfo,
                                             @Query("_UPassword") String password);


    @GET("/webservice/Androidapp.asmx/UserSaleReportDetailAllDateWise?")
    Call<GetOperatorResponse> UserSaleReportDetailAllDateWise(@Query("_UMobile") String appInfo,
                                                              @Query("_UPassword") String password,
                                                              @Query("_Mobile") String _Mobile);

    @GET("/webservice/Androidapp.asmx/UserSaleReportDetailDateWise?")
    Call<GetOperatorResponse> UserSaleReportDetailDateWise(@Query("_UMobile") String appInfo,
                                                           @Query("_UPassword") String password,
                                                           @Query("_Mobile") String _Mobile);

    @GET("/webservice/Androidapp.asmx/UserSaleReportDetail?")
    Call<GetOperatorResponse> UserSaleReportDetail(@Query("_UMobile") String appInfo,
                                                   @Query("_UPassword") String password,
                                                   @Query("_Mobile") String _Mobile);

    @GET("/webservice/Androidapp.asmx/GetDTHInfo?")
    Call<ROfferResponse> GetCustomerInfo(@Query("mobileno") String OperatorId,
                                         @Query("opid") String ZoneId);

    @GET("/plansservices.asmx/GetCustomerInfo?")
    Call<ROfferResponse> GetCustomerInfo(@Query("umobile") String appInfo,
                                         @Query("token") String Token,
                                         @Query("VcNumber") String OperatorId,
                                         @Query("Opcdoe") String ZoneId);

    @GET("/webservice/Androidapp.asmx/PinSecureAuth?")
    Call<HelloWorldResponse> PinSecureAuth(@Query("_UMobile") String appInfo,
                                            @Query("_UPassword") String oldPassword,
                                            @Query("_Pin") String pin);

     @GET("/webservice/Androidapp.asmx/ChangePassword?")
    Call<HelloWorldResponse> changePassword(@Query("_UMobile") String appInfo,
                                            @Query("_OldPass") String oldPassword,
                                            @Query("_NewPass") String newPassword);

    @GET("/webservice/Androidapp.asmx/ChangePinPassword?")
    Call<HelloWorldResponse> changePinPassword(@Query("_UMobile") String appInfo,
                                               @Query("_OldPass") String newPassword,
                                               @Query("_NewPin") String newPin);

    @GET("/webservice/Androidapp.asmx/Recharge?")
    Call<RechargeResponse> Recharge(@Query("_UMobile") String appInfo,
                                    @Query("_UPassword") String password,
                                    @Query("_Mobile") String mobile,
                                    @Query("_Amount") String amount,
                                    @Query("_OperatorID") String operatorId,
                                    @Query("_STDCode") String stdCode,
                                    @Query("_Key") String key);

    @GET("/webservice/Androidapp.asmx/FundTransfer?")
    Call<RechargeResponse> FundTransfer(@Query("_UMobile") String appInfo,
                                        @Query("_UPassword") String password,
                                        @Query("_Mobile") String mobile,
                                        @Query("_Amount") String amount,
                                        @Query("_FundType") String fundType);

    @POST("/webservice/Androidapp.asmx/PaymentRequest")
    @FormUrlEncoded
    Call<RechargeResponse> PaymentRequest(@Field("_UMobile") String appInfo,
                                          @Field("_UPassword") String password,
                                          @Field("_AccountNo") String accountNumber,
                                          @Field("_TransactionID") String transactionId,
                                          @Field("_Amount") String amount,
                                          @Field("_FundType") String fundType,
                                          @Field("_Remark") String _Remark,
                                          @Field("_BankName") String _BankName,
                                          @Field("_AmountINWord") String _AmountINWord,
                                          @Field("_ImgSlip1") String _ImgSlip1);

 // @POST("/webservice/Androidapp.asmx/PaymentRequest")
   // @FormUrlEncoded

     @GET("/webservice/Androidapp.asmx/PaymentRequest?")
    Call<RechargeResponse> PaymentRequest(@Query("_UMobile") String _UMobile,
                                          @Query("_UPassword") String _UPassword,
                                          @Query("_AccountNo") String _AccountNo,
                                          @Query("_TransactionID") String _TransactionID,
                                          @Query("_Amount") String _Amount,
                                          @Query("_FundType") String _FundType,
                                          @Query("_TransactionType") String _TransactionType);


    @GET("/webservice/Androidapp.asmx/LastRechargeStatus?")
    Call<RechargeReportResponse> LastRechargeStatus(@Query("_UMobile") String appInfo,
                                                    @Query("_UPassword") String password,
                                                    @Query("_RechargeMobile") String mobile,
                                                    @Query("_Child_Mobileno") String childAmount,
                                                    @Query("_FromDate") String _FromDate,
                                                    @Query("_ToDate") String _ToDate);

    @GET("/webservice/Androidapp.asmx/Ledger?")
    Call<LedgerReportResponse> Ledger(@Query("_UMobile") String appInfo,
                                      @Query("_UPassword") String password,
                                      @Query("_FromDate") String fromDate,
                                      @Query("_ToDate") String toDate,
                                      @Query("_Search") String _Search);

    @GET("/webservice/Androidapp.asmx/DMRTransaction?")
    Call<DMRReportResponse> DMRTransaction(@Query("_UMobile") String appInfo,
                                           @Query("_UPassword") String password,
                                           @Query("_FromDate") String fromDate,
                                           @Query("_ToDate") String toDate,
                                           @Query("_AccontNo") String account);

    @GET("/webservice/Androidapp.asmx/CommissionSlab?")
    Call<CommissionSlabResponse> CommissionSlab(@Query("_UMobile") String appInfo,
                                                @Query("_UPassword") String password);

    @GET("/webservice/Androidapp.asmx/DisputeReport?")
    Call<DisputeResponse> DisputeReport(@Query("_UMobile") String appInfo,
                                        @Query("_UPassword") String password);

    @GET("/webservice/Androidapp.asmx/FundReceiveStatus?")
    Call<FundRecResponse> FundReceiveStatus(@Query("_UMobile") String appInfo,
                                            @Query("_UPassword") String password,
                                            @Query("FromDate") String FromDate,
                                            @Query("ToDate") String ToDate);

    @GET("/webservice/Androidapp.asmx/FundReceiveStatementDateWise?")
    Call<FundRecResponse> FundReceiveStatementDateWise(@Query("_UMobile") String appInfo,
                                                       @Query("_UPassword") String password,
                                                       @Query("_MobileNo") String _MobileNo);

    @GET("/webservice/Androidapp.asmx/FundTransferStatus?")
    Call<FundRecResponse> FundTransferStatement(@Query("_UMobile") String appInfo,
                                                @Query("_UPassword") String password,
                                                @Query("FromDate") String FromDate,
                                                @Query("ToDate") String ToDate,
                                                @Query("_Child_Mobileno") String childNumber);

    @GET("/webservice/Androidapp.asmx/BalanceCheck?")
    Call<BalanceCheckResponse> BalanceCheck(@Query("_UMobile") String appInfo,
                                            @Query("_UPassword") String password);

    @GET("/webservice/Androidapp.asmx/CheckBalanceDownline?")
    Call<BalanceCheckResponse> CheckBalanceDownline(@Query("_UMobile") String appInfo,
                                                    @Query("_UPassword") String password,
                                                    @Query("_Child_MobileNo") String childNumber);

    @GET("/webservice/Androidapp.asmx/GetUserDayBook?")
    Call<UserDayBookResponse> GetUserDayBook(@Query("_UMobile") String appInfo,
                                             @Query("_UPassword") String password,
                                             @Query("_Child_MobileNo") String childNumber,
                                             @Query("_FromDate") String fromDate);

    @GET("/webservice/Androidapp.asmx/MarkDispute?")
    Call<DisputeResponse> MarkDispute(@Query("_UMobile") String appInfo,
                                      @Query("_UPassword") String password,
                                      @Query("_TransId") String RID,
                                      @Query("_Remark") String remarkText);


    @GET("/webservice/Androidapp.asmx/Refund?")
    Call<LoginSenderResponse> Refund(@Query("_UMobile") String appInfo,
                                     @Query("_UPassword") String password,
                                     @Query("_SenderMobileNo") String senderMobile,
                                     @Query("_VenderID") String venderId);

    @GET("/webservice/Androidapp.asmx/VerifyRefund?")
    Call<LoginSenderResponse> VerifyRefund(@Query("_UMobile") String appInfo,
                                           @Query("_UPassword") String password,
                                           @Query("_SenderMobileNo") String senderMobile,
                                           @Query("_VenderID") String venderId,
                                           @Query("_OTP") String otp,
                                           @Query("_TransactionID") String transactionId);

    @GET("/webservice/Androidapp.asmx/GetBeneficiary?")
    Call<LoginSenderResponse> GetBeneficiary(@Query("_UMobile") String appInfo,
                                             @Query("_UPassword") String password,
                                             @Query("_SenderMobileNo") String senderNumber);

    @GET("/webservice/Androidapp.asmx/DeleteBeneficiary?")
    Call<LoginSenderResponse> DeleteBeneficiary(@Query("_UMobile") String appInfo,
                                                @Query("_UPassword") String password,
                                                @Query("_SenderMobileNo") String senderNumber,
                                                @Query("_RecipientId") String recipientId);

    @GET("/webservice/Androidapp.asmx/GetSender?")
    Call<LoginSenderResponse> GetSender(@Query("_UMobile") String appInfo,
                                        @Query("_UPassword") String password,
                                        @Query("_SenderMobileNo") String senderNumber);

    @GET("/webservice/Androidapp.asmx/CreateSender?")
    Call<LoginSenderResponse> CreateSender(@Query("_UMobile") String appInfo,
                                           @Query("_UPassword") String password,
                                           @Query("_SenderMobileNo") String senderNumber,
                                           @Query("_SenderName") String senderName);

    @GET("/webservice/Androidapp.asmx/VerifySender?")
    Call<LoginSenderResponse> VerifySender(@Query("_UMobile") String appInfo,
                                           @Query("_UPassword") String password,
                                           @Query("_SenderMobileNo") String senderNumber,
                                           @Query("_SenderName") String senderName,
                                           @Query("_OTP") String otp);

    @GET("/webservice/Androidapp.asmx/VerifyBeneficiary?")
    Call<LoginSenderResponse> VerifyBeneficiary(@Query("_UMobile") String appInfo,
                                                @Query("_UPassword") String password,
                                                @Query("_SenderMobileNo") String senderNumber,
                                                @Query("_BankAccount") String bankAccount,
                                                @Query("_BackCode") String bankCode);

    @GET("/webservice/Androidapp.asmx/SendMoney?")
    Call<LoginResponse> SendMoney(@Query("_UMobile") String appInfo,
                                  @Query("_UPassword") String password,
                                  @Query("_SenderMobileNo") String senderNumber,
                                  @Query("_BankAccount") String bankAccount,
                                  @Query("_Amount") String amount,
                                  @Query("_Recipientid") String recipientId,
                                  @Query("_Channel") String channel);

    @GET("/webservice/Androidapp.asmx/AddBeneficiary?")
    Call<LoginSenderResponse> AddBeneficiary(@Query("_UMobile") String appInfo,
                                             @Query("_UPassword") String password,
                                             @Query("_SenderMobileNo") String senderNumber,
                                             @Query("_BeneName") String beneName,
                                             @Query("_BeneMobileNo") String beneMobileNumber,
                                             @Query("_BeneBankAccount") String accountNumber,
                                             @Query("_BeneBankCode_IFSC") String bankIFSC,
                                             @Query("ValidateStatus") String validationStatus,
                                             @Query("BankId") String bankId);

    @GET("/webservice/Androidapp.asmx/GetOTP_ForBBPSOutletRegistration")
    Call<RegisterResponse> OutletRegistartionOTP(@Query("_UMobile") String _UMobile,
                                                 @Query("_UPassword") String _UPassword,
                                                 @Query("MobileNo") String MobileNo);


    @GET("/webservice/Androidapp.asmx/BBPSOutletRegistration")
    Call<RegisterResponse> OutletRegistration(@Query("_UMobile") String _UMobile,
                                              @Query("_UPassword") String _UPassword,
                                              @Query("MobileNo") String MobileNo,
                                              @Query("OutletName") String OutletName,
                                              @Query("Name") String Name,
                                              @Query("Email") String Email,
                                              @Query("Pancard") String Pancard,
                                              @Query("Pincode") String Pincode,
                                              @Query("Address") String Address,
                                              @Query("Otp") String Otp,
                                              @Query("latlong") String Latlong);

    @GET("/webservice/Androidapp.asmx/ViewBill?")
    Call<RechargeInfoResponse> GetViewBill(@Query("_UMobile") String _UMobile,
                                           @Query("_UPassword") String _UPassword,
                                           @Query("_Mobile") String _Mobile,
                                           @Query("_OpCode") String _OpCode,
                                           @Query("_OutletId") String _OutletId);


    @GET("/webservice/Androidapp.asmx/BankList?")
    Call<BankListResponse> BankList(@Query("_UMobile") String appInfo,
                                    @Query("_UPassword") String password);

    @GET("/webservice/Androidapp.asmx/GetElectricityInfo?")
    Call<RechargeInfoResponse> GetElectricityInfo(@Query("mobileno") String mobileno,
                                                  @Query("opid") String opid);

    @GET("/webservice/Androidapp.asmx/CheckDTHInfo?")
    Call<RechargeInfoResponse> GetDTHInfo(@Query("MobileNo") String mobileno,
                                          @Query("RofferCode") String opid);
  @GET("/webservice/Androidapp.asmx/DTHHeavyRefresh?")
    Call<RechargeInfoResponse> DTHHeavyRefresh(@Query("MobileNo") String mobileno,
                                          @Query("RofferCode") String opid);

    @GET("/ws/appdthgetcust.asp")
    Call<ResponseBody> htmlDate(@Query("RechMob") String RechMob,
                                @Query("OPC") String OPC,
                                @Query("ApiToken") String ApiToken);


    @GET("/webservice/Androidapp.asmx/UserCreation?")
    Call<RegisterResponse> UserCreation(@Query("_UMobile") String UMobile,
                                        @Query("_UPassword") String Password,
                                        @Query("_Name") String Name,
                                        @Query("_Email") String Email,
                                        @Query("_Mobile") String Mobile,
                                        @Query("_PIN") String PIN);

    @GET("/webservice/Androidapp.asmx/DTH_Ticket_Open?")
    Call<ReversalOpenListResponse> DTH_Ticket_Open(@Query("_UMobile") String appInfo,
                                                   @Query("_UPassword") String password,
                                                   @Query("_TransId") String transId);

    @GET("/webservice/Androidapp.asmx/DTH_Ticket_Closed?")
    Call<ReversalOpenListResponse> DTH_Ticket_Closed(@Query("_UMobile") String appInfo,
                                                     @Query("_UPassword") String password,
                                                     @Query("_TransId") String transId);

    @GET("/webservice/Androidapp.asmx/DTH_GetDataByTransactionId?")
    Call<ReversalOpenListResponse> DTH_GetDataByTransactionId(@Query("_UMobile") String appInfo,
                                                              @Query("_UPassword") String password,
                                                              @Query("_TransactionId") String transId);

    @GET("/webservice/Androidapp.asmx/DTH_Ticket?")
    Call<DTHTicketResponse> DTH_Ticket(@Query("_UMobile") String appInfo,
                                       @Query("_UPassword") String password,
                                       @Query("_TransId") String transId);

    @GET("/webservice/Androidapp.asmx/DTH_Response?")
    Call<ChatResponse> DTH_Response(@Query("_UMobile") String appInfo,
                                    @Query("_UPassword") String password,
                                    @Query("_TransId") String transId);

    @GET("/webservice/Androidapp.asmx/DTH_Insert_Response?")
    Call<HelloWorldResponse> DTH_Insert_Response(@Query("_UMobile") String appInfo,
                                                 @Query("_UPassword") String password,
                                                 @Query("_TicketId") String ticketId,
                                                 @Query("_Reply") String reply);

    @GET("/webservice/Androidapp.asmx/DTH_Insert_Ticket?")
    Call<HelloWorldResponse> DTH_Insert_Ticket(@Query("_UMobile") String appInfo,
                                               @Query("_UPassword") String password,
                                               @Query("_TransId") String transId,
                                               @Query("_RightRechargeId") String rightId,
                                               @Query("_Remark") String remark);

    @GET("/webservice/Androidapp.asmx/DTH_ApproveTicket?")
    Call<HelloWorldResponse> DTH_ApproveTicket(@Query("_UMobile") String appInfo,
                                               @Query("_UPassword") String password,
                                               @Query("_TicketId") String ticketId,
                                               @Query("_TransId") String transId);

    @GET("/webservice/Androidapp.asmx/DTH_RejectTicket?")
    Call<HelloWorldResponse> DTH_RejectTicket(@Query("_UMobile") String appInfo,
                                              @Query("_UPassword") String password,
                                              @Query("_TicketId") String ticketId,
                                              @Query("_TransId") String transId);

    @GET("/webservice/Androidapp.asmx/KeyUpdate?")
    Call<KeyUpdateResponse> KeyUpdate(@Query("_UMobile") String appInfo,
                                      @Query("_UPassword") String password,
                                      @Query("_DeviceId") String deviceId);

   /* @GET("/webservice/Androidapp.asmx/Roffer?")
    Call<ROfferResponse> RofferPlans(@Query("code") String OpCode,
                                     @Query("Number") String Number);*/
   @GET("/webservice/Androidapp.asmx/CheckOffer?")
   Call<ROfferResponse> RofferPlans(@Query("RofferCode") String OpCode,
                                    @Query("MobileNo") String Number);

    // Adding Popup Data
    @GET("/webservice/Androidapp.asmx/GetPopupAfterLogin?")
    Call<BalanceCheckResponse> Popupdata(@Query("_UMobile") String appInfo,
                                         @Query("_UPassword") String password);
  @GET("/webservice/Androidapp.asmx/BellNotification?")
    Call<BalanceCheckResponse> BellNotification(@Query("_UMobile") String appInfo,
                                         @Query("_UPassword") String password);

  /*   @GET("/webservice/Androidapp.asmx/BookDTH?")
    Call<ROfferResponse> BookDTH(@Query("OPCODE") String OpCode,
                                     @Query("packtype") String packtype,
                                     @Query("package") String Package,
                                     @Query("boxtype") String boxtype,
                                     @Query("amount") String amount,
                                     @Query("name") String name,
                                     @Query("Address") String address,
                                     @Query("PINCode") String pincode,
                                     @Query("MobileNo1") String mobileno1,
                                     @Query("Landmark") String landmark,
                                     @Query("HouseNo") String houseno,
                                     @Query("MobileNo2") String mobileno2,
                                     @Query("UserId") String userid);
                                      @GET("/webservice/Androidapp.asmx/BookDTH?") */


  //_Appid:
  //_ShopName:
  //_Name:
  //_Mobile:
  //_WhatsMobile:
  //_Address:
  //_District:
  //_State:
  //_PIN:
  //_AadharNo:
  //_PANNo
  @GET("/webservice/Androidapp.asmx/Signup?")
    Call<RegisterResponse> Signup(@Query("_Appid") String _Appid,
                                     @Query("_ShopName") String _ShopName,
                                     @Query("_Name") String _Name,
                                     @Query("_Mobile") String _Mobile,
                                     @Query("_WhatsMobile") String _WhatsMobile,
                                     @Query("_Address") String _Address,
                                     @Query("_District") String _District,
                                     @Query("_State") String _State,
                                     @Query("_PIN") String _PIN,
                                     @Query("_AadharNo") String _AadharNo,
                                     @Query("_PANNo") String _PANNo ,
                                     @Query("_RoleId") String _RoleId,
                                  @Query("_Emaild") String _EmailId
                                      );
// @GET("/webservice/B2BSecureService.asmx/UserCreation?")
 @GET("/webservice/Androidapp.asmx/UserCreation?")
    Call<RegisterResponse> UserCreation(@Query("_UMobile") String _Appid,
                                        @Query("_UPassword") String _UPassword,
                                     @Query("_ShopName") String _ShopName,
                                     @Query("_Name") String _Name,
                                     @Query("_Mobile") String _Mobile,
                                     @Query("_WhatsMobile") String _WhatsMobile,
                                     @Query("_Address") String _Address,
                                     @Query("_District") String _District,
                                     @Query("_State") String _State,
                                     @Query("_PIN") String _PIN,
                                     @Query("_AadharNo") String _AadharNo,
                                     @Query("_PANNo") String _PANNo,
                                     @Query("_RoleId") String _RoleId,
                                     @Query("_Emaild") String _EmailId
                                      );
 //_Appid:
    //_ShopName:
    //_Name:
    //_Mobile:
    //_WhatsMobile:
    //_Address:
    //_District:
    //_State:
    //_PIN:
    //_AadharNo:
    //_PANNo:
    //_EmailId:
//    :
//    :
//    _ShopName:
//    _Name:
//    _Mobile:
//    _WhatsMobile:
//    _Address:
//    _District:
//    _State:
//    _PIN:
//    _AadharNo:
//    _PANNo:
//    _RoleId:
//

    @Headers("Content-Type: application/json")
    @POST("PG/ChoosePaymentGateway?")
    Call<PaymentChooseResponse> ChoosePaymentGateway(@Body ChoosePaymentGatewayRequest request);

    @Headers("Content-Type: application/json")
    @POST("PGCallbackResponse/RazorPaySuccess?")
    Call<BasicResponse> RazorPaySuccess(@Body RazorpayTransactionUpdateRequest request);

///
    @Headers("Content-Type: application/json")
    @POST("PG/PGatewayTransaction?")
    Call<PaymentTransactionResponse> GatewayTransaction(@Body GatewayTransactionRequest request);

    @Headers("Content-Type: application/json")
    @POST("PG/PayTMTransactionUpdate")
    Call<BasicResponse> PayTMTransactionUpdate(@Body PayTMTransactionUpdateRequest request);

}