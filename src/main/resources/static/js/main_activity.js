
var $user_group;
var $target_group;
var main = function(){
	console.log("Hi Friends, main_activity");

	$user_group = $("#user-group").find(".state-printer div");
	$target_group = $("#target-group").find(".state-printer div");

	//Random combination...
	var initial_states = "";
	for(var i=0; i < 4 ;i++)
		if( Math.random() > 0.5 )	initial_states += "1";
		else						initial_states += "0";
	from_string_to_gui(initial_states, $target_group, true);

	$user_group.click(function(){
		var url = "./data/toggle/" + $(this).data('led-target');
		$.post(url, function () {
			update_led_states();
		});
	});

	update_led_states();

	$("#finish-btn").click( finish_activity );
};
$(main);

function update_led_states() {
	$.getJSON('./data/led-query', function(data){
		from_string_to_gui( data.res, $user_group );
	});
}
function from_string_to_gui(states, $group, labeled) {
	if( labeled === undefined )
		labeled = false;
	for (var i = 0 ; i < states.length ; i++){
		var $item = $($group[i]).parent();
		$item.removeClass('state-on');
		$item.removeClass('state-off');
		if( states.charAt(i) === '1' ){
			$item.addClass('state-on');
			if(labeled)	$item.find('span').text("On");
		}else{
			$item.addClass('state-off');
			if(labeled)	$item.find('span').text("Off");
		}
	}
}

function finish_activity() {
	var total = 4;
	var good = 0;
	for(var i=0;i<4;i++){
		var $user_item = $($user_group[i]).parent();
		var $target_item = $($target_group[i]).parent();
		if(
			( $target_item.hasClass('state-on') && $user_item.hasClass('state-on') )
			||
			( $target_item.hasClass('state-off') && $user_item.hasClass('state-off') )
		)	good++;
	}
	var grade = good / total;
	send_grades( grade );
}

function send_grades( grades ) {
	$.post("./send_grades", {
		lis_outcome_service_url : lis_outcome_service_url,
		key : key,
		secret : secret,
		lis_result_sourcedid : lis_result_sourcedid,
		grade : grades
	}, function (data) {
		data = JSON.parse(data);
		if ( data.res === 'Ok' ){
			$.get("./data/reset");
			alert("See You");
			window.location.replace(go_back_url);
		}else
			alert( data.res );
	});
}
