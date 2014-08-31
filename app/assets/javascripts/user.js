
function updateUsers() {

	myJsRoutes.controllers.Experimenter
			.searchUsers()
			.ajax(
					{

						method : 'post',
						data : $(".form-horizontal").serialize(),
						success : function(data) {
							if (data == null)
								$("#amount")
										.text(
												"Es gab leider keine Treffer für die angegebenen Kriterien.")
							else {
								$("#users").html("")
								var obj = jQuery.parseJSON(data);
								console.log(obj)
								$("#amount").text(obj.length)
								$.each(obj, function(key, value) {
									el = $("#user_dr").clone();
									el.show()
									el.find(".name").text(value.name);
									el.data("user-id", value.id);
									el.find("#edit").prop('href',
											'user_edit/' + value.id)
									$("#users").append(el);
								});
							}
						}

					});
}

$(document).on('ready', function() {
	updateUsers();
})
$("form :input, :checkbox").change(function() {
	updateUsers();
});