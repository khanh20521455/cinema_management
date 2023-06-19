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
$( '#multiple-select-custom-field' ).select2( {
    theme: "bootstrap-5",
    width: $( this ).data( 'width' ) ? $( this ).data( 'width' ) : $( this ).hasClass( 'w-100' ) ? '100%' : 'style',
    placeholder: $( this ).data( 'placeholder' ),
    closeOnSelect: false,
    tags: true
} );