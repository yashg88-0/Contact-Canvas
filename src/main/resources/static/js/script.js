// script.js
function toggleSideBar() {
	console.log("toggleSideBar called");  // Debugging line
	if ($(".side-bar").is(":visible")) {
		$(".side-bar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		$(".side-bar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
}

ClassicEditor
	.create(document.querySelector('#description'))
	.catch(error => {
		console.error(error);
	});

function confirmDelete(event) {
	// Ask the user for confirmation
	if (!confirm("Are you sure you want to delete this contact?")) {
		// Prevent the default action (deleting) if the user cancels
		event.preventDefault();
	}
}