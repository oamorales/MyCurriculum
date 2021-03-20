package com.oamorales.myresume.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.oamorales.myresume.R;
import com.oamorales.myresume.adapters.SkillsRecyclerAdapter;
import com.oamorales.myresume.databinding.FragmentSkillsBinding;
import com.oamorales.myresume.models.Skill;
import com.oamorales.myresume.viewmodels.SkillsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.realm.RealmResults;

public class SkillsFragment extends Fragment {

    private FragmentSkillsBinding binding;
    private SkillsViewModel viewModel;
    private AppCompatEditText newSkill;
    private SkillsRecyclerAdapter adapter;

    public SkillsFragment() { /** Required empty public constructor */  }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSkillsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SkillsViewModel.class);
        viewModel.init();
        viewModel.getSkillsList().observe(getViewLifecycleOwner(), new Observer<RealmResults<Skill>>() {
            @Override
            public void onChanged(RealmResults<Skill> skills) {
                adapter.notifyDataSetChanged();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(requireContext());
        binding.skillsRecyclerView.setLayoutManager(manager);
        adapter = new SkillsRecyclerAdapter(requireActivity(), R.layout.skills_card_view, viewModel.getSkillsList().getValue());
        binding.skillsRecyclerView.setAdapter(adapter);
        /** FAB onClick event */
        binding.fabNewSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSkill();
            }
        });
    }

    private void addSkill(){
        newSkill = new AppCompatEditText(requireActivity());
        MaterialAlertDialogBuilder materialBuilder = new MaterialAlertDialogBuilder(requireActivity());
        materialBuilder.setTitle(R.string.new_skill).setView(newSkill)
                .setMessage(R.string.type_new_skill)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = Objects.requireNonNull(newSkill.getText()).toString();
                        if (value.isEmpty())
                            Toast.makeText(requireActivity(), getText(R.string.empty_field), Toast.LENGTH_SHORT).show();
                        else {
                            Skill skill = new Skill(value);
                            viewModel.saveSkill(skill);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();

        /*AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("New Skill").setView(newSkill)
                .setMessage("Type the new skill")
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = Objects.requireNonNull(newSkill.getText()).toString();
                        if (value.isEmpty()){
                            Toast.makeText(requireActivity(), "FIELD MUST NOT BE EMPTY", Toast.LENGTH_SHORT).show();
                        }else {
                            Skill skill = new Skill(value);
                            //DBManager.insert(skill, requireContext());
                            viewModel.saveSkill(skill);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
