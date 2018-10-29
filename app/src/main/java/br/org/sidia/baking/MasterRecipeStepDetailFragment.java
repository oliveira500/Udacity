package br.org.sidia.baking;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import br.org.sidia.baking.adapter.IngredientAdapter;
import br.org.sidia.baking.model.Ingredient;
import br.org.sidia.baking.model.Step;
import br.org.sidia.baking.utils.Network;

public class MasterRecipeStepDetailFragment extends Fragment{

    private TextView txtShortDescription;
    private TextView txtDescription;
    private TextView txtNoInternet;
    private TextView txtNoVideo;
    private RecyclerView rv_Ingredients;
    private boolean statusClickIngredient = false;
    private Step step;
    private Ingredient[] ingredients;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    public long position = C.TIME_UNSET;
    public boolean state;

    public static final String CLICK = "click";
    public static final String STEP = "step";
    public static final String INGREDIENT = "ingredient";
    public static final String PLAYER = "player";
    public static final String PLAYERSTATE = "playerState";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        txtShortDescription = view.findViewById(R.id.txt_recipe_step);
        txtDescription = view.findViewById(R.id.txt_description);
        txtNoInternet = view.findViewById(R.id.txt_warning_recipe_detail);
        txtNoVideo = view.findViewById(R.id.txt_warning_recipe_detail_video);
        simpleExoPlayerView = view.findViewById(R.id.player_recipe_step);
        rv_Ingredients = view.findViewById(R.id.rv_ingredients);

        if (savedInstanceState != null){
            statusClickIngredient = savedInstanceState.getBoolean(CLICK);
            step = (Step) savedInstanceState.getSerializable(STEP);
            ingredients = (Ingredient[]) savedInstanceState.getSerializable(INGREDIENT);
        }

        if (statusClickIngredient){

            rv_Ingredients.setVisibility(View.VISIBLE);
            txtShortDescription.setVisibility(View.GONE);
            txtDescription.setVisibility(View.GONE);
            txtNoInternet.setVisibility(View.GONE);
            txtNoVideo.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.GONE);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            rv_Ingredients.setLayoutManager(linearLayoutManager);

            IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext());
            ingredientAdapter.setIngredients(ingredients);

            rv_Ingredients.setAdapter(ingredientAdapter);

        } else {

            rv_Ingredients.setVisibility(View.GONE);
            txtShortDescription.setText(step.getShortDescription());
            txtDescription.setText(step.getDescription());

            if (step.getVideoURL().isEmpty()) {
                txtNoVideo.setVisibility(View.VISIBLE);
                simpleExoPlayerView.setVisibility(View.GONE);
            } else {
                txtNoVideo.setVisibility(View.GONE);
                simpleExoPlayerView.setVisibility(View.VISIBLE);

                if (Network.verifyConnection(getContext())) {
                    txtNoInternet.setVisibility(View.GONE);
                    simpleExoPlayerView.setVisibility(View.VISIBLE);

                    initializePlayer(Uri.parse(step.getVideoURL()));

                    if (savedInstanceState != null) {
                        position = savedInstanceState.getLong(PLAYER, position);
                        mExoPlayer.seekTo(position);
                        state = savedInstanceState.getBoolean(PLAYERSTATE);
                        mExoPlayer.setPlayWhenReady(state);
                    }

                } else {
                    txtNoInternet.setVisibility(View.VISIBLE);
                    simpleExoPlayerView.setVisibility(View.GONE);
                }
            }

        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CLICK, statusClickIngredient);
        outState.putSerializable(STEP, step);
        outState.putSerializable(INGREDIENT, ingredients);

        if (!statusClickIngredient && (simpleExoPlayerView.getVisibility() == View.VISIBLE)) {
            outState.putLong(PLAYER, mExoPlayer.getCurrentPosition());
            outState.putBoolean(PLAYERSTATE, mExoPlayer.getPlayWhenReady());
        }
    }

    public void setStep(Step step){
        this.step = step;
    }

    public void setIngredient(Ingredient[] ingredients){ this.ingredients = ingredients; }

    public void setStatusClickIngredient(boolean status){ this.statusClickIngredient = status; }



    public void initializePlayer(Uri media){
        if (mExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getActivity(), getString(R.string.baking_app));
            MediaSource mediaSource = new ExtractorMediaSource(media, new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mExoPlayer != null)
            releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mExoPlayer != null){
            initializePlayer(Uri.parse(step.getVideoURL()));
            mExoPlayer.seekTo(position);
            mExoPlayer.getPlayWhenReady();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mExoPlayer != null) {
            position = mExoPlayer.getCurrentPosition();
            releasePlayer();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if (mExoPlayer != null)
            releasePlayer();
    }
}
