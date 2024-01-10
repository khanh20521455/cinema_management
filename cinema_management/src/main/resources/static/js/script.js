console.log("This is script file")
$('.toggle-password').click(function(){
  $(this).toggleClass('fa-eye fa-eye-slash');
  var input = $($(this).attr('toggle'));
  if (input.attr('type') == 'password') {
    input.attr('type', 'text');
  } else {
    input.attr('type', 'password');
  }
});
const toggleSidebar = () => {

    if($(".sidebar").is(":visible")){

        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");

    }

    else{
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
};

const searchByMovieNameAdmin = () => {
    //console.log("Searching...")
    let query = $("#searchInput").val();

    if(query == ""){
        $(".search-result-movie").hide();
    }else{

        // search
        console.log(query);
        let url = `http://localhost:8082/search_admin/${query}`;

        fetch(url)
            .then( (response) => {
                return response.json();
            })
            .then( (data) => {

                let text=`<div class='list-group'>`;

                data.forEach( (movie) => {
                    text+=`<a href='/admin/update_movie/${movie.id}' class='list-group-item list-group-action'> ${movie.name} </a>`
                });

                text+=`</div>`;

                $(".search-result-movie").html(text);
                $(".search-result-movie").show();
            });
    }
};


const searchByMovieNameUser = () => {
    let query = $("#searchInput").val();
    $(".playing-movie-list").show();
    $(".upcoming-movie-list").show();
    if (query == "") {
        $(".search-result").hide();
    } else {
        let url = `http://localhost:8082/search/${query}`;

        fetch(url)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                let searchResults = [];

                data.forEach((movie) => {
                    let movieItem = `<div class="card movie-item" style="width:300px">
                                        <img class="card-img-top img-movie" src="/img/${movie.poster}" alt="" style="width:100%">
                                        <div class="card-body">
                                            <h4 class="card-title boldText">${movie.name}</h4>
                                            <a class="text-decoration-none" href="#" th:href="@{'/user/movie_detail/'+${movie.id}}"
                                               style="margin-right:40px">
                                                <button class="btn btn-outline-warning boldText" style="width: 100px">Chi tiết</button>
                                            </a>
                                            <a class="text-decoration-none" href="#" th:href="@{'/user/buy_ticket/'+${movie.id}}">
                                                <button class="btn btn-outline-danger boldText" style="width: 100px">Đặt vé</button>
                                            </a>
                                        </div>
                                    </div>`;
                    searchResults.push(movieItem);
                });
                $(".playing-movie-list").hide();
                $(".upcoming-movie-list").hide();
                $(".search-result").html(searchResults.join(""));
                $(".search-result").show();
            });
    }
};
function getProvince() {
   let url = `http://localhost:8082/queryProvince`;
   fetch(url)
       .then(response => response.json())
       .then(data => {
           const select = document.getElementById("province_showtimes_field");
           data.forEach(result => {
               const option = document.createElement("option");
               option.textContent  = result;
               option.value=result;
               select.add(option);
           });
       })
       .catch(error => {
           console.error(error);
       });
}
function onSelectProvince() {
   document.getElementById("theater_showtimes_field").options.length = 1;
   document.getElementById("date_showtimes_field").value = 0;
   document.getElementById("typeScreen_showtimes_field").options.length = 1;
   document.getElementById("time_showtimes_field").options.length = 1;
   document.getElementById("numberSeat_field").value = 0;
   document.getElementById("seatList_field").value = "";
   $(".seatNumber").removeClass("seatSelected");
   let date = document.getElementById("date_showtimes_field").value;
   let movieId = document.getElementById("movie-id-data").innerText;
   let province = document.getElementById("province_showtimes_field").value
   let url = `http://localhost:8082/queryTheater/?id=${movieId}&province=${province}`;
   fetch(url)
       .then(response => response.json())
       .then(data => {
           const select = document.getElementById("theater_showtimes_field");
           data.forEach(result => {
               const option = document.createElement("option");
               option.textContent  = result.name;
               option.value=result.id;
               select.add(option);
           });
       })
       .catch(error => {
           console.error(error);
       });
}
function onSelectTheater(){
   document.getElementById("date_showtimes_field").value = 0;
   document.getElementById("typeScreen_showtimes_field").options.length = 1;
   document.getElementById("time_showtimes_field").options.length = 1;
   document.getElementById("numberSeat_field").value = 0;
   document.getElementById("seatList_field").value = "";
   $(".seatNumber").removeClass("seatSelected");
}
function onSelectDate() {
      document.getElementById("typeScreen_showtimes_field").options.length = 1;
      document.getElementById("time_showtimes_field").options.length = 1;
      document.getElementById("numberSeat_field").value = 0;
      document.getElementById("seatList_field").value = "";
      $(".seatNumber").removeClass("seatSelected");
   let date = document.getElementById("date_showtimes_field").value;
   let movieId = document.getElementById("movie-id-data").innerText;
   let theaterId = document.getElementById("theater_showtimes_field").value;
   let url = `http://localhost:8082/queryTypeScreen/?id=${movieId}&theaterId=${theaterId}&date=${date}`;
   fetch(url)
       .then(response => response.json())
       .then(data => {
           const select = document.getElementById("typeScreen_showtimes_field");
           data.forEach(result => {
               const option = document.createElement("option");
               option.textContent  = result;
               option.value=result;
               select.add(option);
           });
       })
       .catch(error => {
           console.error(error);
       });
}

$(document).ready(function() {
    // Reset form khi quay lại
    $("form")[0].reset();
});
function onSelectTypeScreen(){
      document.getElementById("time_showtimes_field").options.length = 1;
      document.getElementById("numberSeat_field").value = 0;
      document.getElementById("seatList_field").value = "";
      $(".seatNumber").removeClass("seatSelected");
   let date = document.getElementById("date_showtimes_field").value;
   let movieId = document.getElementById("movie-id-data").innerText;
   let typeScreen=document.getElementById("typeScreen_showtimes_field").value;
   let theaterId = document.getElementById("theater_showtimes_field").value;
   let url = `http://localhost:8082/queryShowtimes/?id=${movieId}&theaterId=${theaterId}&date=${date}&typeScreen=${typeScreen}`;
      fetch(url)
          .then(response => response.json())
          .then(data => {
              const select = document.getElementById("time_showtimes_field");
              data.forEach(result => {
                  const option = document.createElement("option");
                  option.textContent  = result.time;
                  option.value=result.id;
                  select.add(option);
              });
          })
          .catch(error => {
              console.error(error);
          });
}
function onSelectTime(){
      document.getElementById("numberSeat_field").value = 0;
      document.getElementById("seatList_field").value = "";
      $(".seatNumber").removeClass("seatSelected");
    let showtimesId=document.getElementById("time_showtimes_field").value;
    let url = `http://localhost:8082/querySeatNotSelect/?id=${showtimesId}`;
    fetch(url)
              .then(response => response.json())
              .then(data => {
                  data.forEach(result => {
                      let element = document.getElementById(result.seat);
                      if (element) {
                        element.classList.add("seatUnavailable");
                      }
                  });
              })
              .catch(error => {
                  console.error(error);
              });
}

var selectedSeats = [];
$(".seatNumber").click(
        function () {
            if (!$(this).hasClass("seatUnavailable")){
                if ($(this).hasClass("seatSelected")) {
                    $(this).removeClass("seatSelected");
                    var seatId = $(this).attr('id');
                    var index = selectedSeats.indexOf(seatId);
                    if (index > -1) {
                        selectedSeats.splice(index, 1);
                    }
                }
                else {
                    if (selectedSeats.length < document.getElementById("numberSeat_field").value){
                    $(this).addClass("seatSelected");
                    var seatId = $(this).attr('id');
                    selectedSeats.push(seatId);
                    }

                }
            }
        });
$("form").submit(function() {
    var selectedSeatIds = selectedSeats.map(Number);
    $("#seatList_field").val(selectedSeatIds);
});
var count;
function starmark(item)
{
    count=item.id[0];
    var subid= item.id.substring(1);
    for(var i=0;i<5;i++)
    {
        if(i<count){
        document.getElementById((i+1)+subid).style.color="orange";
        }
        else{
        document.getElementById((i+1)+subid).style.color="grey";
        }
    }
    document.getElementById("rating-star").setAttribute("value", count);

}

function result(){
}

function formatInputValue(input) {
    var inputValue = input.value;
    var numericValue = inputValue.replace(/[^\d.]/g, '');
    var formattedValue = formatNumberWithSpaces(numericValue);
    input.value = formattedValue;
}
function formatNumberWithSpaces(number) {
    var spacedNumber = Number(number).toLocaleString('en-US', { useGrouping: true });
    return spacedNumber.replace(/,/g, ',');
}

function formatDate(input) {
    // Parse the input value as a date
    var selectedDate = new Date(input.value);

    // Format the date as dd/MM/yyyy
    var formattedDate = ("0" + selectedDate.getDate()).slice(-2) + "/" +
                        ("0" + (selectedDate.getMonth() + 1)).slice(-2) + "/" +
                        selectedDate.getFullYear();

    // Set the formatted date back to the input value
    input.value = formattedDate;
}
document.addEventListener('DOMContentLoaded', function () {
  var checkbox = document.getElementById('flexSwitchCheckDefault');
  checkbox.addEventListener('change', function () {
   var isChecked = checkbox.checked;
   var checkboxLabel = document.querySelector('.form-check-input');
   var totalMoney = parseInt(document.getElementById("total-money").innerHTML);
   const tMoney= totalMoney
   let bookingId = document.getElementById("booking-id-data").innerText;
    if (isChecked) {
      checkboxLabel.style.backgroundColor = '#FF7171'; // Thay đổi thành màu đỏ
      checkboxLabel.style.color = ' #FF7171'
      checkboxLabel.style.borderColor = ' #FF7171'
      checkbox.setAttribute('isPoint', 'true');
            let url = `http://localhost:8082/queryTotal/?total=${totalMoney}&bookingId=${bookingId}`;
            fetch(url)
              .then(response => response.json())
              .then(result => {
              var spacedNumber = Number(result).toLocaleString('en-US', { useGrouping: true });
                totalMoney=result
                 document.getElementById("total-money").innerHTML = spacedNumber.replace(/,/g, ',');
                 document.getElementById("total-money").value = result;
              });

    } else {
      // Change color back when unchecked
      checkboxLabel.style.backgroundColor = ''; // Set to an empty string to remove inline style
      checkbox.setAttribute('isPoint', 'false');
      let url = `http://localhost:8082/queryTotalAdd/?total=${totalMoney}&bookingId=${bookingId}`;
                 fetch(url)
                   .then(response => response.json())
                   .then(result => {
                    var spacedNumber = Number(result).toLocaleString('en-US', { useGrouping: true });
                     totalMoney=result
                      document.getElementById("total-money").innerHTML = spacedNumber.replace(/,/g, ',');
                      document.getElementById("total-money").value = result;
                   });
    }
  });
});


