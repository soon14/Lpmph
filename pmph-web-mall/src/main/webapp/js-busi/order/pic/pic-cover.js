$(function() {
	/**
	 * 绑定图片 向右旋转90度
	 */
	var rotateAngle = 0;//初始化旋转角度 
	 
	$("#imgRotate").live('click',function(){
		if(rotateAngle == 360){
	    	rotateAngle = 0;
	    }
		rotateAngle +=90;
		$(this).rotate({
			angle:rotateAngle
		});
	});   
});

function imgModal(_this) {
 	$("#imgShow").empty();
 	bDialog.open({
		height: $(window).height()*0.8,
		width: $(window).width()*0.8
	},'<img src=' + _this.src + ' id="imgRotate" style="cursor: pointer" class="img-thumbnail">');
}