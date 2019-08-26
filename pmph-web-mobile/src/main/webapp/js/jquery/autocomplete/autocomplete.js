/**
 A jQuery plugin for search hints

 Author: Lorenzo Cioni - https://github.com/lorecioni
 */

(function($) {
	$.fn.autocomplete = function(params) {

		//Selections
		var currentSelection = -1;
		var currentProposals = [];

		//Default parameters
		params = $.extend({
			hints: [],
			placeholder: '',
			width: 200,
			height: 16,
			showButton: true,
			buttonText: 'Search',
			inputSet:null,
			onSubmit: function(text){},
			onBlur: function(){},
			onChange:null
		}, params);

		//Build messagess
		this.each(function() {
			//Container
			var searchContainer = $('<div></div>')
				.addClass('autocomplete-container')
				.css('height', params.height * 2);

			//Text input		
			var input = $('<input type="text" autocomplete="off" name="query" id="searchTxt">')
				.attr('placeholder', params.placeholder)
				.addClass('autocomplete-input')
				.css({
					'width' : params.width,
					'height' : params.height
				});

			if(params.showButton){
				input.css('border-radius', '3px 0 0 3px');
			}

			//Proposals
			var proposals = $('<div></div>')
				.addClass('proposal-box')
				.css('width', params.width + 18)
				.css('top', input.height() + 20);
			var proposalList = $('<ul></ul>')
				.addClass('proposal-list');

			proposals.append(proposalList);
			input.bind("change paste keyup", function(e){

		////		alert((e.keyCode&&e.keyCode != 27
			//		&& e.keyCode != 38 && e.keyCode != 40));
			//	if(e.which&&e.which != 27
			//			&& e.which != 38 && e.which != 40){
					currentProposals = [];
					currentSelection = -1;
					/*** zqr modify **/
					if(params.onChange) {
						var timeout;
						var $this = $(this);
						if (!timeout) {
							timeout = window.setTimeout(function () {
								params.onChange(params, $this, proposalList, currentProposals)
								window.clearTimeout(timeout);
								timeout = null;
							}, 5);
						}
					}
					else if(input.val() != ''){
						proposalList.empty();
						var word = "^" + input.val() + ".*";
						for(var test in params.hints){
							if(params.hints[test].match(word)){
								currentProposals.push(params.hints[test]);
								var element = $('<li></li>')
									.html(params.hints[test])
									.addClass('proposal')
									.click(function(){
										input.val($(this).html());
										proposalList.empty();
										params.onSubmit(input.val());
									})
									.mouseenter(function() {
										$(this).addClass('selected');
									})
									.mouseleave(function() {
										$(this).removeClass('selected');
									});
								proposalList.append(element);
							}
						}

					}
			//	}
			});

			input.blur(function(e){
				currentSelection = -1;
				//proposalList.empty();
				params.onBlur();
			});

			searchContainer.append(input);
			searchContainer.append(proposals);

			if(params.showButton){
				//Search button
				var button = $('<div></div>')
					.addClass('autocomplete-button')
					.html(params.buttonText)
					.css({
						'height': params.height + 2,
						'line-height': params.height + 'px'
					})
					.click(function(){
						proposalList.empty();
						/*
						 * zhuqr???
						 * params.onSubmit(input.val());
						 * */
						params.onSubmit(input);
					});
				searchContainer.append(button);
			}

			$(this).append(searchContainer);

			if(params.showButton){
				//Width fix
				searchContainer.css('width', params.width + button.width() + 50);
			}
		});

		return this;
	};

})(jQuery);