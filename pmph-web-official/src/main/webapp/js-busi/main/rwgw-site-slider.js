/**
 * 人卫网站  轮播效果实现
 */
$(function() {

	var jcarousel = $('.jcarousel');
    jcarousel.live('jcarousel:reload jcarousel:create', function () {
         var carousel = $(this),
                 width = carousel.innerWidth();
         if (width >= 600) {
             width = width / 8;
         } else if (width >= 350) {
             width = width / 2;
         }
         carousel.jcarousel('items').css('width', Math.ceil(width) + 'px');
     })
     .jcarousel({
         wrap: 'circular'
     });

   $('.swipe-page.prev')
     .jcarouselControl({
         target: '-=1'
     });

   $('.swipe-page.next')
     .jcarouselControl({
         target: '+=1'
     });

});