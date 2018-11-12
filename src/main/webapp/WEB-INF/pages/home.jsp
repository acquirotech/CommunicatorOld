<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

              <!-- page content -->
            <div class="right_col" role="main">
              <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                        <div class="dashboard_graph">

                            <div class="row x_title">
                                <div class="col-md-12">
                                    <h3>Monthly Activities</h3>
                                </div>
                            </div>

                            <div class="col-md-12 col-sm-12 col-xs-12">
                             	<div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
			                        <div class="left"></div>
			                        <div class="right">
	                            		<span class="count_top"><i class="fa fa-user"></i> Average Ticket Size</span>
	                            		<div class="count">${monthlyTxnGMV}</div>
                          			</div>
                    			</div>
                    			<div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
                        			<div class="left"></div>
                        			<div class="right">
                            		<span class="count_top"><i class="fa fa-clock-o"></i> No. of Merchants Boarded <small>(Today)</small></span>
                            		<div class="count">${todayMerchantBoarded}</div>
                         	   		</div>
                    			</div>
			                    <div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
			                        <div class="left"></div>
			                        <div class="right">
			                            <span class="count_top"><i class="fa fa-user"></i> No. of Merchants Boarded <small>(Last 30 Days)</small> </span>
			                            <div class="count">${monthlyMchntBoarded}</div>
			                            </div>
					            </div>
		                    	 <div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
		                       		<div class="left"></div>
		                        	<div class="right">
		                            <span class="count_top"><i class="fa fa-user"></i> Total Merchants Boarded </span>
		                            <div class="count">${totalnoOfMerchant}</div>
		                            </div>
		                    	</div>
              </div>
                <div class="clearfix"></div>
                        </div>
                    </div>

                </div>
             <br />
  <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                        <div class="dashboard_graph">

                            <div class="row x_title">
                                <div class="col-md-12">
                                    <h3>Today's Activities</h3>
                                </div>
                            </div>

                            <div class="col-md-12 col-sm-12 col-xs-12">

                             <div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
                        <div class="left"></div>
                        <div class="right">
                            <span class="count_top"><i class="fa fa-user"></i> Transaction Count</span>
                            <div class="count">${todayTxnCount}</div>
                          
                        </div>
                    </div>
                    <div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
                        <div class="left"></div>
                        <div class="right">
                            <span class="count_top"><i class="fa fa-clock-o"></i> Transaction Sales</span>
                            <div class="count"> ${todaySales}</div>
                          </div>
                    </div>
                    <div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
                        <div class="left"></div>
                        <div class="right">
                            <span class="count_top"><i class="fa fa-user"></i> Transaction MDR </span>
                            <div class="count">${todayMdr}</div>
                            </div>
                    </div>
                    <div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
                        <div class="left"></div>
                        <div class="right">
                            <span class="count_top"><i class="fa fa-user"></i> Transaction Commission</span>
                            <div class="count">${todayCommission}</div>
                          </div>
                    </div>   
                            </div>

                            <div class="clearfix"></div>
                        </div>
                    </div>

                </div>
           <br />
             <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                        <div class="dashboard_graph">

                            <div class="row x_title">
                                <div class="col-md-12">
                                    <h3>Monthly Activities <small>(Last 30 Days)</small></h3>
                                </div>
                            </div>

                            <div class="col-md-12 col-sm-12 col-xs-12">

                             <div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
                        <div class="left"></div>
                        <div class="right">
                            <span class="count_top"><i class="fa fa-user"></i> Transaction Count</span>
                            <div class="count">${monthlyTxnCount}</div>
                          
                        </div>
                    </div>
                    <div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
                        <div class="left"></div>
                        <div class="right">
                            <span class="count_top"><i class="fa fa-clock-o"></i> Transaction Sales</span>
                            <div class="count"> ${monthlyTxnSales}</div>
                          </div>
                    </div>
                    <div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
                        <div class="left"></div>
                        <div class="right">
                            <span class="count_top"><i class="fa fa-user"></i> Transaction MDR </span>
                            <div class="count">${monthlyTxnMdr}</div>
                            </div>
                    </div>
                    <div class="animated flipInY col-md-3 col-sm-4 col-xs-4 tile_stats_count">
                        <div class="left"></div>
                        <div class="right">
                            <span class="count_top"><i class="fa fa-user"></i> Transaction Commission</span>
                            <div class="count">${monthlyTxnCommission}</div>
                          </div>
                    </div>   
                            </div>
                              <div class="clearfix"></div>
                        </div>
                    </div>

                </div>
                <br />
<jsp:include page="/jsp/footer.jsp" />