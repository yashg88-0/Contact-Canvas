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

function confirmDelete(cId) {
	Swal.fire({
		title: "Are you sure?",
		text: "You won't be able to revert this!",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#3085d6",
		cancelButtonColor: "#d33",
		confirmButtonText: "Yes, delete it!"
	}).then((result) => {
		if (result.isConfirmed) {
			Swal.fire({
				title: "Deleted",
				text: "Your contact is successfully deleted!",
				icon: "success"
			}).then(() => {
				setTimeout(() => {
					// Redirect to the server to handle the deletion
					window.location.href = "/user/delete-contact/" + cId;
				}, 100); // Delay of 0.1 seconds
			});
		} else {
			Swal.fire({
				title: "Cancelled",
				text: "Your contact is still safe!",
				icon: "info"
			});
		}
	});
}

function validatePasswords() {
	var password = document.getElementById("form3Example4c").value;
	var confirmPassword = document.getElementById("form3Example4cd").value;
	if (password !== confirmPassword) {
		document.getElementById("passwordMismatchError").style.display = "block";
		return false;
	} else {
		document.getElementById("passwordMismatchError").style.display = "none";
		return true;
	}
}