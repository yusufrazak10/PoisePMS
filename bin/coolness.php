<?php
/*
 Plugin Name: Coolness
 Plugin URI: https://www.hyperiondev.com
 Description: A plugin that keeps track of post views
 Version: 1.0
 Author: Yusuf Razak
 Author URI: http://localhost/wordpress/wp-admin/index.php
 License: UNLICENSED
 */

function coolness_set_initial_view_count($post_id) {
    // Only run on single posts and ensure the post is published
    if (is_single() && 'post' === get_post_type($post_id) && 'publish' === get_post_status($post_id)) {
        // Check if the post has no view count set yet.
        if (get_post_meta($post_id, 'coolness_views', true) === '') {
            // Set the initial view count to 0 for new posts only
            update_post_meta($post_id, 'coolness_views', 0);
        }
    }
}

// Hook into the 'save_post' action to set the view count to 0 when a new post is created
add_action( 'save_post', 'coolness_set_initial_view_count' );

function coolness_new_view() {
    // Only interested in single posts (not pages)
    if ( !is_single() )
        return null;
        
        global $post;
        
        // Get current view count from post meta
        $views = get_post_meta( $post->ID, 'coolness_views', true );
        
        
        if ( ! $views ) {
            $views = 0; // if $views is undefined.
        }
        
        $views++;
        
        // Update the post's view count in the database
        update_post_meta( $post->ID, 'coolness_views', $views );
        
        // Return the updated view count
        return $views;
}


// Hook the function to the wp_head action
add_action( 'wp_head', 'coolness_new_view' );

function coolness_views() {
    global $post;
    $views = get_post_meta( $post->ID, 'coolness_views', true );
    
    if ( ! $views )
        $views = 0;
        
        // Set the text dynamically based on the view count.
        if ( $views == 1 ) {
            return "This post has 1 view.";
        } else {
            return "This post has " . $views . " views.";
        }
}


function coolness_list() {
    $searchParams = [
        'posts_per_page' => 10,
        'post_type' => 'post',
        'post_status' => 'publish',
        'meta_key' => 'coolness_views',
        'orderby' => 'meta_value_num',
        'order' => 'DESC'
    ];
    
    $list = new WP_Query($searchParams);
    
    if ($list->have_posts()) {
        global $post;
        echo '<ol>';
        while ($list->have_posts()) {
            $list->the_post();
            // Get current view count from post meta
            $views = get_post_meta($post->ID, 'coolness_views', true);
            echo '<li><a href="' . get_permalink($post->ID) . '">';
            the_title();
            // Display view count next to each link.
            echo ' - ' . $views . '</a></li>';
        }
        echo '</ol>';
    }
}


?>


