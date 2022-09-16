package cu.entumovil.snb.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.managers.TeamManager;
import cu.entumovil.snb.core.models.Player;
import cu.entumovil.snb.core.models.PlayerDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerStatsFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + PlayerStatsFragment.class.getSimpleName();

    private PlayerStatsFragment fragment;

    private Context context;

    private static Player PLAYER;

    private View rootView;

    private LinearLayout layoutHitterOffensive, layoutPitcherOffensive;

    private TextView lblPlayerAve, txtPlayerDetailAVE, txtGames, txtInnings, txtHits, txtDouble, txtTriple, txtHomeruns;

    private TextView txtRuns, txtBB, txtSO, txtOBP, txtOPS, txtOuts, txtDoublePlay, txtErrors, txtFAVE;

    private TextView txtPitcherGames, txtPitcherInnings, txtWins, txtLost, txtSaved, txtCleanRuns;

    private TextView txtWHIP, txtPitcherBB, txtPitcherSO, txtVSAVE;

    public PlayerStatsFragment() { }

    public static PlayerStatsFragment newInstance(Player aPlayer) {
        PlayerStatsFragment fragment = new PlayerStatsFragment();
        PLAYER = aPlayer;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_stats, container, false);
        rootView = view;
        context = view.getContext();
        initialiceGUIObjects();
        loadPlayerStatistics();
        return view;
    }

    private void loadPlayerStatistics() {
        try {
            TeamManager manager = new TeamManager();
            final Call<ArrayList<PlayerDetails>> call = manager.getPlayerById(PLAYER.getId());
            call.enqueue(new Callback<ArrayList<PlayerDetails>>() {
                @Override
                public void onResponse(Call<ArrayList<PlayerDetails>> call, Response<ArrayList<PlayerDetails>> response) {
                    try {
                        if (null != response.body()) {
                            PlayerDetails p =  response.body().get(0);
                            if (null != p.getBAT()) {
                                lblPlayerAve.setText("Promedio");
                                txtPlayerDetailAVE.setText(p.getAVE());
                                txtGames.setText(p.getJJ());
                                txtInnings.setText(p.getINN());
                                txtHits.setText(p.getH());
                                txtDouble.setText(p.getTubeyes());
                                txtTriple.setText(p.getTriples());
                                txtHomeruns.setText(p.getHR());
                                txtRuns.setText(p.getA());
                                txtBB.setText(p.getBB());
                                txtSO.setText(p.getSO());
                                txtOBP.setText(p.getOBP());
                                txtOPS.setText(p.getOPS());
                            } else if (null != p.getLAN()) {
                                lblPlayerAve.setText("Promedio de carreras limpias");
                                txtPlayerDetailAVE.setText(p.getPCL());
                                layoutHitterOffensive.setVisibility(View.GONE);
                                layoutPitcherOffensive.setVisibility(View.VISIBLE);
                                txtPitcherGames.setText(p.getJJ());
                                txtPitcherInnings.setText(p.getINN());
                                txtWins.setText(p.getJG());
                                txtLost.setText(p.getJP());
                                txtSaved.setText(p.getJS());
                                txtCleanRuns.setText(p.getCL());
                                txtWHIP.setText(p.getWHIP());
                                txtPitcherBB.setText(p.getPBB());
                                txtPitcherSO.setText(p.getPSO());
                                txtVSAVE.setText(p.getAVE_Op());
                            }
                            txtOuts.setText(p.getO());
                            txtDoublePlay.setText(p.getDP());
                            txtErrors.setText(p.getE());
                            txtFAVE.setText(p.getFAVE());
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.message_not_exists_info), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<PlayerDetails>> call, Throwable throwable) {
                    Toast.makeText(getActivity(), getString(R.string.message_timeout), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public String toString() {
        return SNBApp.application.getResources().getString(R.string.label_tab_stats_numbers);
    }

    private void initialiceGUIObjects() {
        lblPlayerAve = (TextView) rootView.findViewById(R.id.lblPlayerAve);
        layoutHitterOffensive = (LinearLayout) rootView.findViewById(R.id.layoutHitterOffensive);
        txtPlayerDetailAVE = (TextView) rootView.findViewById(R.id.txtPlayerDetailAVE);
        txtGames = (TextView) rootView.findViewById(R.id.txtGames);
        txtInnings = (TextView) rootView.findViewById(R.id.txtInnings);
        txtHits = (TextView) rootView.findViewById(R.id.txtHits);
        txtDouble = (TextView) rootView.findViewById(R.id.txtDouble);
        txtTriple = (TextView) rootView.findViewById(R.id.txtTriple);
        txtHomeruns = (TextView) rootView.findViewById(R.id.txtHomeruns);
        txtRuns = (TextView) rootView.findViewById(R.id.txtRuns);
        txtBB = (TextView) rootView.findViewById(R.id.txtBB);
        txtSO = (TextView) rootView.findViewById(R.id.txtSO);
        txtOBP = (TextView) rootView.findViewById(R.id.txtOBP);
        txtOPS = (TextView) rootView.findViewById(R.id.txtOPS);

        layoutPitcherOffensive = (LinearLayout) rootView.findViewById(R.id.layoutPitcherOffensive);
        txtPitcherGames = (TextView) rootView.findViewById(R.id.txtPitcherGames);
        txtPitcherInnings = (TextView) rootView.findViewById(R.id.txtPitcherInnings);
        txtWins = (TextView) rootView.findViewById(R.id.txtWins);
        txtLost = (TextView) rootView.findViewById(R.id.txtLost);
        txtSaved = (TextView) rootView.findViewById(R.id.txtSaved);
        txtCleanRuns = (TextView) rootView.findViewById(R.id.txtCleanRuns);
        txtWHIP = (TextView) rootView.findViewById(R.id.txtWHIP);
        txtPitcherBB = (TextView) rootView.findViewById(R.id.txtPitcherBB);
        txtPitcherSO = (TextView) rootView.findViewById(R.id.txtPitcherSO);
        txtVSAVE = (TextView) rootView.findViewById(R.id.txtVSAVE);

        txtOuts = (TextView) rootView.findViewById(R.id.txtOuts);
        txtDoublePlay = (TextView) rootView.findViewById(R.id.txtDoublePlay);
        txtErrors = (TextView) rootView.findViewById(R.id.txtErrors);
        txtFAVE = (TextView) rootView.findViewById(R.id.txtFAVE);
    }
}
