/**
 *
 */

$('.d-trans').on('click', function() {
	$(this).css('pointer-events', 'none');
});

$(document).on("change keydown keyup paste cut", "#auto-height", function(evt) {
	var min_height = 1;
	$(evt.target).height(min_height);
	$(evt.target).height(evt.target.scrollHeight);
});

function textLengthCount(str, max_length, id_count) {
	if (1 <= str.length && str.length <= max_length) {
		var target = document.getElementById("inputLength" + id_count)
		target.innerHTML = str.length + "/" + max_length;
		var button = document.getElementById("validated-btn" + id_count)
		target.style.color = 'black';
		button.disabled = false

	} else if (str.length == 0) {
		var target = document.getElementById("inputLength" + id_count)
		target.innerHTML = str.length + "/" + max_length;
		var button = document.getElementById("validated-btn" + id_count)
		button.disabled = true
	} else {
		var target = document.getElementById("inputLength" + id_count)
		target.innerHTML = str.length + "/" + max_length + "文字数制限を超えています";
		var button = document.getElementById("validated-btn" + id_count)
		button.disabled = true

		target.style.color = 'red';
	}
}

function textLengthCountForInputs(str, max_length, id_count) {
	if (str.length <= max_length) {
		var target = document.getElementById("inputLength" + id_count)
		var button = document.getElementById("validated-btn")
		target.innerHTML = str.length + "/" + max_length;
		target.style.color = 'black';
		button.disabled = false

	} else {
		var target = document.getElementById("inputLength" + id_count)
		target.innerHTML = str.length + "/" + max_length + "文字数制限を超えています";
		var button = document.getElementById("validated-btn")
		button.disabled = true

		target.style.color = 'red';
	}
}

function textLengthCountForDm(str, max_length) {
	if (1 <= str.length && str.length <= max_length) {
		var target = document.getElementById("inputLength")
		target.innerHTML = "";
		var button = document.getElementById("validated-btn")
		target.style.color = 'black';
		button.disabled = false
	} else if (str.length == 0) {
		var target = document.getElementById("inputLength")
		target.innerHTML = "";
		var button = document.getElementById("validated-btn")
		button.disabled = true
	} else {
		var target = document.getElementById("inputLength")
		target.innerHTML = "文字数制限を超えています";
		var button = document.getElementById("validated-btn")
		button.disabled = true

		target.style.color = 'red';
	}
}

window.onload = function scrollInner() {
	let target = document.getElementById('scroll-inner');
	target.scrollIntoView(false);
}

$('.detail-btn').on('click', function() {
	$(this).toggleClass('active');
});
