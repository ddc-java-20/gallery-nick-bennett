package edu.cnm.deepdive.nasaapod.controller;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.squareup.picasso.Picasso;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.databinding.FragmentDetailsBinding;
import edu.cnm.deepdive.nasaapod.viewmodel.ApodViewModel;
import javax.inject.Inject;

@AndroidEntryPoint
public class DetailsFragment extends DialogFragment {

  @Inject
  Picasso picasso;

  private long apodId;
  private FragmentDetailsBinding binding;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    apodId = DetailsFragmentArgs.fromBundle(getArguments())
        .getApodId();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    Context context = requireContext();
    binding = FragmentDetailsBinding.inflate(LayoutInflater.from(context), null, false);
    return new Builder(context)
        .setIcon(R.drawable.info)
        .setTitle(R.string.details_title)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.ok, (dlg, wh) -> {})
        .create();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ApodViewModel viewModel = new ViewModelProvider(requireActivity())
        .get(ApodViewModel.class);
    viewModel
        .getApod()
        .observe(getViewLifecycleOwner(), (apod) -> {
          binding.title.setText(apod.getTitle().strip());
          String copyright = apod.getCopyright();
          if (copyright != null) {
            binding.copyright.setText(copyright.strip());
            binding.copyright.setVisibility(View.VISIBLE);
          } else {
            binding.copyright.setVisibility(View.GONE);
          }
          binding.description.setText(apod.getDescription().strip());
          picasso
              .load(apod.getLowDefUrl().toString())
              .into(binding.thumbnail);
        });
    viewModel.setApodId(apodId);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}
