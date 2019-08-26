/**
 * site-info-nav.vm 的业务js
 * 从文件服务器获取静态html代码
 * zhanbh
 */
$(function() {
	var SiteInfoNav = {
		//页面初始化
		"init" : function() {
			var siteInfoId = "";
			var item = $('#item').val();
			if (!item) {
				item = '0';
			}
			$("#navH li").each(function(index, el) {
				debugger
				if (item == index) {
					siteInfoId = $(el).attr('data-site-info-id');
					$(el).addClass("active");
				}

			});

			//网站信息id
			//var siteInfoId = $(".siteinfoitem.active").attr("data-site-info-id");
			if (siteInfoId) {
				var $objContent = $(".siteinfo" + siteInfoId);
				//显示对应的静态文件信息
				$objContent.attr("style", "");
				var staticUrl = $objContent.find("input.siteinfostaticUrl")
						.val();
				if (staticUrl) {
					SiteInfoNav.getHtml(staticUrl, $objContent);
				}
			}
		},
		//根据静态文件路径，填充富文本内容。
		"getHtml" : function(url, $objContent) {
			if (url && (typeof $objContent).toLowerCase() === "object") {
				//var url = "http://192.168.1.102:8080/imageServer/static/html/55f68185cc964eefd42d1429";
				$.ajax({
					url : url,
					async : true,
					type : "get",
					dataType : 'jsonp',
					jsonp : 'jsonpCallback',//注意此处写死jsonCallback
					success : function(data) {
						$objContent.empty();
						if (data && data.result) {
							$objContent.html(data.result);
						}

					}
				});
			}
		}
	}

	//初始化页面
	SiteInfoNav.init();

	//绑定网站信息点击事件
	$(".siteinfoitem").live("click", function() {
		var $this = $(this);
		if (!$this.hasClass("active")) {//当点击的不是当前选中的时候  进行切换
			var $siteInfos = $(".siteinfoitem");
			var siteInfoId = $this.attr("data-site-info-id");
			//修改网站信息的样式
			$siteInfos.removeClass("active");
			$this.addClass("active");

			//隐藏所有网站信息内容
			$(".siteinfodiv").attr("style", "display:none;");

			var $objContent = $(".siteinfo" + siteInfoId);

			//显示对应的静态文件信息
			$objContent.attr("style", "");

			//如果未从文件服务器获取静态文件，则去获取
			var staticUrl = $objContent.find("input.siteinfostaticUrl").val();
			if (staticUrl) {
				SiteInfoNav.getHtml(staticUrl, $objContent);
			}

		}
	});
});
