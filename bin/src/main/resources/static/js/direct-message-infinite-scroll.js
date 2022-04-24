/**
 *
 */

$(function() {
	var IScontentItems = '.dm__item';
	var IScontent = '.dm-body-main';
	var ISlink = '.pager__next';
	var ISlinkarea = '.pager';
	var loadingFlag = false;

	$(".message").on('load scroll', function() {
		if (!loadingFlag) {
			var winHeight = $(".message").height();
			var scrollPos = $(".message").scrollTop();

			if (scrollPos <= 0) {
				loadingFlag = true;

				var nextPage = $(ISlink).attr('href');
				if (nextPage != undefined) {
					var linkPos = $(".dm-body-main").offset().top;
					$(".message").scrollTop(linkPos);
				}
				$(ISlink).remove();
				$.ajax({
					type : 'GET',
					url : nextPage,
					dataType : 'html'
				}).done(function(data) {
					var nextLink = $(data).find(ISlink);
					var contentItems = $(data).find(IScontentItems);

					if (nextPage != undefined) {
						$(IScontent).prepend(contentItems);
					}
					if (nextLink.length > 0) {

						$(ISlinkarea).append(nextLink);
						loadingFlag = false;
					} else {
						$(ISlinkarea).remove();
					}

				}).fail(function() {
					alert('ページの取得に失敗しました。');
				});

			}
		}
	});
});
