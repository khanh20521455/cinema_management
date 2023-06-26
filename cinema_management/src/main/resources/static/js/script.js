console.log("This is script file")

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
    //console.log("Searching...")
    let query = $("#searchInput").val();
  
    if(query == ""){
        $(".search-result").hide();
    }else{

        // search
        console.log(query);
        let url = `http://localhost:8082/search/${query}`;

        fetch(url)
            .then( (response) => {
                return response.json();
            })
            .then( (data) => {

                let text=`<div class='list-group'>`;

                data.forEach( (movie) => {

                    text+=`<a href='/user/movie_detail/${movie.id}' class='list-group-item list-group-action'> ${movie.name} </a>`
                });

                text+=`</div>`;

                $(".search-result").html(text);
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
                  const select = document.getElementById("seat_field");
                  select.size = document.getElementById("numberSeat_field").value;
                  data.forEach(result => {
                      const option = document.createElement("option");
                      option.textContent  = result.seat;
                      option.value=result.seat;
                      select.add(option);
                  });
              })
              .catch(error => {
                  console.error(error);
              });
}
    var count;
    function starmark(item)
    {
        count=item.id[0];
        sessionStorage.starRating = count;
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



function result()
{

}


