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
        $(".search-result").hide();
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

                $(".search-result").html(text);
                $(".search-result").show();
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
                                            <h4 class="card-title">${movie.name}</h4>
                                            <a href="/user/movie_detail/${movie.id}" style="margin-right:40px">
                                                <button class="btn btn-outline-warning">View detail</button>
                                            </a>
                                            <a href="/user/buy_ticket/${movie.id}">
                                                <button class="btn btn-outline-danger">Buy Ticket</button>
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
function onSelectDate() {
   let date = document.getElementById("date_showtimes_field").value;
   let movieId = document.getElementById("movie-id-data").innerText;
   let url = `http://localhost:8082/query/?id=${movieId}&date=${date}`;
   fetch(url)
       .then(response => response.json())
       .then(data => {
           const select = document.getElementById("typeScreen_showtimes_field");
           data.forEach(result => {
               const option = document.createElement("option");
               option.textContent  = result.typeScreen;
               option.value=result.typeScreen;
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
   let date = document.getElementById("date_showtimes_field").value;
   let movieId = document.getElementById("movie-id-data").innerText;
   let typeScreen=document.getElementById("typeScreen_showtimes_field").value;
   let url = `http://localhost:8082/queryShowtimes/?id=${movieId}&date=${date}&typeScreen=${typeScreen}`;
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
function onSelectNumberSeat(){
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
$(".table-statistic").hide();
function statistic_movie() {

    let start = document.getElementById("movie_start_field").value;
    let end = document.getElementById("movie_end_field").value;
    let url = `http://localhost:8082/bookingForStatistic_ticket_desc/?start=${start}&end=${end}`;
    fetch(url)
        .then(response => response.json())
        .then(data => {
            let resultList = [];
            data.forEach(result => {
                let resultItem = `<tr>
                                     <td>${result[0]}</td>
                                     <td><img class="my_movie_poster" src="/img/${result[1]}" alt="movie poster"></td>
                                     <td>${result[2]}</td>
                                     <td>${result[3]}</td>
                                     <td>${result[4]}</td>
                                 </tr>`;
                resultList.push(resultItem);
            });

            $(".table-statistic").show();
            $(".statistic_movie_result").html(resultList.join(""));
        })
        .catch(error => {
            console.error(error);
        });
}

function statistic_ticket_onclick(){
    let start = document.getElementById("movie_start_field").value;
    let end = document.getElementById("movie_end_field").value;
    let button= document.getElementById("button_statistic_ticket").innerText;
    if(button === "Lượt vé tăng dần"){
        document.getElementById("button_statistic_ticket").innerText="Lượt vé giảm dần";
         var url = `http://localhost:8082/bookingForStatistic_ticket_asc/?start=${start}&end=${end}`;
    }
    else{
        document.getElementById("button_statistic_ticket").innerText="Lượt vé tăng dần";
        var url = `http://localhost:8082/bookingForStatistic_ticket_desc/?start=${start}&end=${end}`;
    }
    fetch(url)
        .then(response => response.json())
        .then(data => {
            let resultList = [];
            data.forEach(result => {
                let resultItem = `<tr>
                                     <td>${result[0]}</td>
                                     <td><img class="my_movie_poster" src="/img/${result[1]}" alt="movie poster"></td>
                                     <td>${result[2]}</td>
                                     <td>${result[3]}</td>
                                     <td>${result[4]}</td>
                                 </tr>`;
                resultList.push(resultItem);
            });

            $(".table-statistic").show();
            $(".statistic_movie_result").html(resultList.join(""));
        })
        .catch(error => {
            console.error(error);
        });
}
function statistic_revenue_onclick(){
    let start = document.getElementById("movie_start_field").value;
    let end = document.getElementById("movie_end_field").value;
    let button= document.getElementById("button_statistic_revenue").innerText;
    if(button === "Doanh thu tăng dần"){
        document.getElementById("button_statistic_revenue").innerText="Doanh thu giảm dần";
        var url = `http://localhost:8082/bookingForStatistic_revenue_asc/?start=${start}&end=${end}`;
    }
    else{
        document.getElementById("button_statistic_revenue").innerText="Doanh thu tăng dần";
        var url = `http://localhost:8082/bookingForStatistic_revenue_desc/?start=${start}&end=${end}`;
    }
    fetch(url)
        .then(response => response.json())
        .then(data => {
            let resultList = [];
            data.forEach(result => {
                let resultItem = `<tr>
                                     <td>${result[0]}</td>
                                     <td><img class="my_movie_poster" src="/img/${result[1]}" alt="movie poster"></td>
                                     <td>${result[2]}</td>
                                     <td>${result[3]}</td>
                                     <td>${result[4]}</td>
                                 </tr>`;
                resultList.push(resultItem);
            });

            $(".table-statistic").show();
            $(".statistic_movie_result").html(resultList.join(""));
        })
        .catch(error => {
            console.error(error);
        });
}


