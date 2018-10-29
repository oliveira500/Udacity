package br.org.sidia.baking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.org.sidia.baking.Adapter.StepAdapter;
import br.org.sidia.baking.Model.Ingredient;
import br.org.sidia.baking.Model.Step;

public class MasterRecipeStepFragment extends Fragment {

    private StepAdapter.StepAdapterOnClickHandler stepAdapterOnClickHandler;

    private Step[] steps;
    private RecyclerView recyclerView;
    private StepAdapter stepAdapter;
    private Ingredient[] ingredients;

    public MasterRecipeStepFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        recyclerView = view.findViewById(R.id.rv_recipe_steps);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        stepAdapter = new StepAdapter(getContext(), stepAdapterOnClickHandler);
        stepAdapter.setSteps(steps);

        recyclerView.setAdapter(stepAdapter);

        return view;
    }

    public void setSteps(Step[] steps){
        this.steps = steps;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }
    public void setStepAdapterOnClickHandler(StepAdapter.StepAdapterOnClickHandler stepAdapterOnClickHandler){
        this.stepAdapterOnClickHandler = stepAdapterOnClickHandler;
    }

}
