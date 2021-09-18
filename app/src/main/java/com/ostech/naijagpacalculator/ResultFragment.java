package com.ostech.naijagpacalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ostech.naijagpacalculator.model.AcademicRecord;
import com.ostech.naijagpacalculator.model.Institution;
import com.ostech.naijagpacalculator.model.Semester;

import java.util.ArrayList;

public class ResultFragment extends Fragment {
    private static final String TAG = ResultFragment.class.getCanonicalName();

    private RecyclerView semestersRecyclerView;
    private ProgressBar cgpaProgressBar;
    private AppCompatTextView cgpaTextView;
    private AppCompatTextView tnuTextView;
    private AppCompatTextView tcpTextView;
    private AppCompatTextView numberOfCoursesTextView;
    private AppCompatTextView remarkTextView;

    private final ArrayList<Semester> semesterList = AcademicRecord.getInstance().getSemesterList();
    private final Institution institution = AcademicRecord.getInstance().getInstitutionType();

    private SemesterAdapter semesterAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        semestersRecyclerView = view.findViewById(R.id.result_semester_recycler_view);
        cgpaProgressBar = view.findViewById(R.id.result_cgpa_progress_bar);
        cgpaTextView = view.findViewById(R.id.result_cgpa_text_view);
        tnuTextView = view.findViewById(R.id.result_tnu_text_view);
        tcpTextView = view.findViewById(R.id.result_tcp_text_view);
        numberOfCoursesTextView = view.findViewById(R.id.result_number_of_courses_text_view);
        remarkTextView = view.findViewById(R.id.result_remark_text_view);

        AcademicRecord.getInstance().calculateCGPA();

        double cgpa = AcademicRecord.getInstance().getCumulativeGradePointAverage();
        double totalCreditUnit = AcademicRecord.getInstance().getTotalCreditUnit();
        double totalGradePoint = AcademicRecord.getInstance().getTotalGradePoint();
        int numberOfCourses = AcademicRecord.getInstance().getNumberOfCourses();
        String remark = institution.getRemark(cgpa);

        int cgpaProgressBarUpperRange = (int) (institution.MAXIMUM_GPA * 100);
        int cgpaProgressBarValue = (int) (cgpa * 100);

        cgpaProgressBar.setMax(cgpaProgressBarUpperRange);
        cgpaProgressBar.setProgress(cgpaProgressBarValue);

        cgpaTextView.setText(String.format("%.2f", cgpa));
        tnuTextView.setText(String.format("%d", totalCreditUnit));
        tcpTextView.setText(String.format("%d", totalGradePoint));
        numberOfCoursesTextView.setText(String.format("%d", numberOfCourses));
        remarkTextView.setText(remark);

        semestersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (semesterAdapter == null) {
            semesterAdapter = new SemesterAdapter(semesterList);
            semestersRecyclerView.setAdapter(semesterAdapter);
        } else {
            semesterAdapter.setSemesters(semesterList);
        }

        return view;
    }   //  end of onCreateView()

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getResources().getString(R.string.gpa_calculator_menu_item_title) + " - " +
                getResources().getString(R.string.gpa_calculator_result_title);

        getActivity().setTitle(title);
    }

    private class SemesterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar gpaProgressBar;
        private AppCompatTextView gpaTextView;
        private AppCompatTextView tnuTextView;
        private AppCompatTextView tcpTextView;
        private AppCompatTextView numberOfCoursesTextView;
        private AppCompatTextView remarkTextView;
        private Semester semester;

        public SemesterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.holder_result_semester, parent, false));
            itemView.setOnClickListener(this);

            gpaProgressBar = itemView.findViewById(R.id.gpa_progress_bar);
            gpaTextView = itemView.findViewById(R.id.gpa_text_view);
            tnuTextView = itemView.findViewById(R.id.tnu_text_view);
            tcpTextView = itemView.findViewById(R.id.tcp_text_view);
            numberOfCoursesTextView = itemView.findViewById(R.id.number_of_courses_text_view);
            remarkTextView = itemView.findViewById(R.id.remark_text_view);
        }

        @Override
        public void onClick(View view) {
        }

        public void bind(Semester semester) {
            this.semester = semester;
            double gpa = semester.getSemesterGPA();
            double totalCreditUnit = semester.getTotalCreditUnit();
            double totalGradePoint = semester.getTotalGradePoint();
            int numberOfCourses = semester.getNumberOfCourses();
            String remark = institution.getRemark(gpa);

            int cgpaProgressBarUpperRange = (int) (institution.MAXIMUM_GPA * 100);
            int cgpaProgressBarValue = (int) (gpa * 100);

            gpaProgressBar.setMax(cgpaProgressBarUpperRange);
            gpaProgressBar.setProgress(cgpaProgressBarValue);

            gpaTextView.setText(String.format("%.2f", gpa));
            tnuTextView.setText(String.format("%d", totalCreditUnit));
            tcpTextView.setText(String.format("%d", totalGradePoint));
            numberOfCoursesTextView.setText(String.format("%d", numberOfCourses));
            remarkTextView.setText(remark);
        }
    }

    private class SemesterAdapter extends RecyclerView.Adapter<SemesterHolder> {
        private ArrayList<Semester> semesters;

        public SemesterAdapter(ArrayList<Semester> semesters) {
            this.semesters = semesters;
        }

        @Override
        public SemesterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SemesterHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SemesterHolder holder, int position) {
            Semester currentSemester = semesters.get(position);
            holder.bind(currentSemester);
        }

        @Override
        public int getItemCount() {
            return semesters.size();
        }

        public void setSemesters(ArrayList<Semester> semesters) {
            this.semesters = semesters;
        }
    }
}   //  end of class
