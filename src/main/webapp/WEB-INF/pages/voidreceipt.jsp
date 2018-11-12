<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>receipt</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<!-- Save for Web Slices (reciept.jpg) -->
<table id="Table_01" width="455" height="1280" border="0" cellpadding="0" cellspacing="0" style="padding:10px;border:1px solid #888">
	<tr>
		<%-- <td colspan="2">
			<c:if test="${receiptType == 'indus'}">
				<img src="http://18.218.17.153:8080/Communicator/resources/images/receipt_03.png" width="455" height="146" alt="">
			</c:if>
			<c:if test="${receiptType == 'rbl'}">
				<img src="http://18.218.17.153:8080/Communicator/resources/images/reciept_01.png" width="455" height="146" alt="">
			</c:if>
			<c:if test="${receiptType == 'ogsbank'}">
				<img src="http://18.218.17.153:8080/Communicator/resources/images/cityUnionBank.PNG" width="455" height="146" alt="">
			</c:if>
		</td> --%>
	<tr>
		<td colspan="2">
			<img src="http://18.218.17.153:8080/Communicator/resources/images/reciept_02.png" width="455" height="8" alt=""></td>
	</tr>
	<tr>
		<td colspan="2" style="text-align: center;color: #008000;font-weight: bold;font-size: 18px;">
			${orgName}</td>
	</tr>
	<tr>
		<td colspan="2" style="text-align: center;color: #888;font-size: 18px;">
			${address}</td>
	</tr>
	<tr>
		<td style="text-align: left;color: #888;font-size: 18px;">
			${emailId}</td>
		<td style="text-align: right;color: #888;font-size: 18px;">
			${rmn}</td>
	</tr>
	<tr>
		<td colspan="2" style="text-align: left;color: #008000;font-size: 18px;font-weight:bold">
			Invoice Summary</td>
	</tr>
	<tr>
		<td style="text-align:left;color: #888;font-size: 18px;padding-left:10px;">
			<span style="font-weight:bold">Date Time</span><br>
			<span>${dateTime}</span>
			
		</td>
		<td style="text-align:right;color: #888;font-size: 18px;padding-right:10px;">
			<span style="font-weight:bold">RRN</span><br>
			<span>${rrNo}</span>			
		</td>
	</tr>
	<tr>
		<td style="text-align:left;color: #888;font-size: 18px;padding-left:10px;">
			<span style="font-weight:bold">TID</span><br>
			<span>${tid}</span>			
		</td>
		<td style="text-align:right;color: #888;font-size: 18px;padding-right:10px;">
			<span style="font-weight:bold">MID</span><br>
			<span>${mid}</span>			
		</td>
	</tr>
	<tr>
		<td style="text-align:left;color: #888;font-size: 18px;padding-left:10px;">
			<span style="font-weight:bold">Batch No</span><br>
			<span>${batchNo}</span>			
		</td>
		<td style="text-align:right;color: #888;font-size: 18px;padding-right:10px;">
			<span style="font-weight:bold">Invoice No.</span><br>
			<span>${invoiceId}</span>			
		</td>
	</tr>
	<tr>
		<td style="text-align:left;color: #888;font-size: 18px;padding-left:10px;">
			<span style="font-weight:bold">Card Type</span><br>
			<span>${cardType}</span>			
		</td>
		<td style="text-align:right;color: #888;font-size: 18px;padding-right:10px;">
			<span style="font-weight:bold">Auth Code</span><br>
			<span>${authCode}</span>
			
		</td>
	</tr>
	<tr>
		<td style="text-align:left;color: #888;font-size: 18px;padding-left:10px;">
			<span style="font-weight:bold">Card Holder Name</span><br>
			<span>${cardHolderName}</span>
			
		</td>
		<td style="text-align:right;color: #888;font-size: 18px;padding-right:10px;">
			<span style="font-weight:bold">Card No.</span><br>
			<span>${cardPanNo}</span>
			
		</td>
	</tr>
	<tr>
		<td style="text-align:left;color: #888;font-size: 18px;padding-left:10px;">
			<span style="font-weight:bold">Billed To</span><br>
			<span>${custPhone}</span><br />
			<span>${custEmail}</span>
			
		</td>
		<td style="text-align:right;color: #888;font-size: 18px;padding-right:10px;">
			<span style="font-weight:bold">Amount</span><br>
			<span>Rs.${amount}</span>
			
		</td>
	</tr>	
	
	
	<tr style="padding:10px;">
		<td colspan="2" style="text-align:center;color: #fff;font-size: 18px;background-color: #008000;">
			${description}</td>
	</tr>
	<tr>
		<td style="text-align:left;color: #000;font-size: 18px;padding-left:10px;">
			<span style="font-weight:bold">Transaction Amount</span></td>
		<td style="text-align:right;color: #000;font-size: 18px;padding-right:10px;">
			<span style="font-weight:bold">Rs. ${amount}</span></td>
	</tr>
	<tr>
		<td colspan="2" style="text-align:center;">
		<img src="http://18.218.17.153:8080/Communicator/resources/images/logo.png" alt="..." width="455" height="146" class="img-circle profile_img">
		</td>
	</tr>
<tr style="padding:10px;">
		<td colspan="2" style="text-align:center;color: #000;font-size: 18px;">
			<span>I Agree to pay the above total amount according </span><br>
			<span>to card issuer agreement.</span>
			 </td>
	</tr>
	<tr>
		<td colspan="2" style="text-align:center;color: #888;font-size: 18px;">
			<span style="font-weight:bold"> Thank you for  your business</span><br>
			<span>Powered by Acquiro Payments.</span><br>
   			<span>www.acquiropayments.com</span><br>
			<!-- <span>App Version: 1.5.4</span> -->
			</td>
	</tr>
</table>
<!-- End Save for Web Slices -->
</body>
</html>