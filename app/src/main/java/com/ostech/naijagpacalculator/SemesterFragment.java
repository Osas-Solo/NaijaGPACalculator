package com.ostech.naijagpacalculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ostech.naijagpacalculator.model.AcademicRecord;
import com.ostech.naijagpacalculator.model.Course;
import com.ostech.naijagpacalculator.model.Institution;
import com.ostech.naijagpacalculator.model.Polytechnic;
import com.ostech.naijagpacalculator.model.Semester;

import java.util.ArrayList;
import java.util.Arrays;

public class SemesterFragment extends Fragment {
    private static final String TAG = SemesterFragment.class.getCanonicalName();
    private static final String ARG_SEMESTER_POSITION = "current_semester";

    private RecyclerView semesterRecyclerView;
    private AppCompatButton previousSemesterButton;
    private AppCompatButton nextSemesterButton;
    private AppCompatButton calculateButton;

    private int semesterPosition;
    private Semester currentSemester;

    private CourseAdapter courseAdapter;

    public static SemesterFragment newInstance(int semesterPosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_SEMESTER_POSITION, semesterPosition);

        SemesterFragment fragment = new SemesterFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        semesterPosition = getArguments().getInt(ARG_SEMESTER_POSITION);
        currentSemester = AcademicRecord.getInstance().getSemesterList().get(semesterPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semester, container, false);

        semesterRecyclerView = view.findViewById(R.id.semester_recycler_view);
        previousSemesterButton = view.findViewById(R.id.previous_semester_button);
        nextSemesterButton = view.findViewById(R.id.next_semester_button);
        calculateButton = view.findViewById(R.id.calculate_button);

        updateButtonsVisibility();

        SemesterPagerActivity semesterPagerActivity = (SemesterPagerActivity) getActivity();

        previousSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterPagerActivity.navigateToPreviousSemester(semesterPosition);
            }
        });

        nextSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterPagerActivity.navigateToNextSemester(semesterPosition);
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToResultActivity();
            }
        });

        semesterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<Course> courseList = currentSemester.getCourseList();

        if (courseAdapter == null) {
            courseAdapter = new CourseAdapter(courseList);
            semesterRecyclerView.setAdapter(courseAdapter);
        } else {
            courseAdapter.setCourses(courseList);
        }

        setHasOptionsMenu(true);

        return view;
    }   //  end of onCreateView()

    private void updateButtonsVisibility() {
        calculateButton.setVisibility(View.INVISIBLE);

        if (semesterPosition == 0) {
            previousSemesterButton.setVisibility(View.INVISIBLE);

            if (semesterPosition == AcademicRecord.getInstance().getNumberOfSemesters() - 1) {
                nextSemesterButton.setVisibility(View.INVISIBLE);
                calculateButton.setVisibility(View.VISIBLE);
            }
        } else if (semesterPosition == AcademicRecord.getInstance().getNumberOfSemesters() - 1) {
            nextSemesterButton.setVisibility(View.INVISIBLE);
            calculateButton.setVisibility(View.VISIBLE);
        } else {
            calculateButton.setVisibility(View.INVISIBLE);
        }
    }

    private void goToResultActivity() {
        Intent resultIntent = ResultActivity.newIntent(getActivity());
        startActivity(resultIntent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(currentSemester.getSemesterName());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateButtonsVisibility();
        getActivity().setTitle(currentSemester.getSemesterName());
    }

    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText semesterCourseCodeEditText;
        private EditText semesterCreditUnitEditText;
        private Spinner semesterGradeSpinner;

        private Course course;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.holder_semester_course, parent, false));
            itemView.setOnClickListener(this);

            semesterCourseCodeEditText = itemView.findViewById(R.id.semester_course_code_edit_text);
            semesterCreditUnitEditText = itemView.findViewById(R.id.semester_credit_unit_edit_text);
            semesterGradeSpinner = itemView.findViewById(R.id.semester_grade_spinner);

            semesterCourseCodeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    course.setCourseCode(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            semesterCreditUnitEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() != 0) {
                        int creditUnit = Integer.parseInt(s.toString());
                        course.setCreditUnit(creditUnit);
                    } else {
                        course.setCreditUnit(0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            semesterGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String grade = semesterGradeSpinner.getSelectedItem().toString();

                    course.setGrade(grade);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onClick(View view) {
        }

        public void bind(Course course) {
            this.course = course;

            semesterCourseCodeEditText.setText(this.course.getCourseCode());

            if (this.course.getCreditUnit() != 0) {
                semesterCreditUnitEditText.setText("" + this.course.getCreditUnit());
            }

            Institution institution = AcademicRecord.getInstance().getInstitutionType();
            String[] gradesArray;

            if (institution instanceof Polytechnic) {
                gradesArray = getResources().getStringArray(R.array.polytechnic_grades);
            } else {
                gradesArray = getResources().getStringArray(
                        R.array.university_and_college_of_education_grades);
            }

            ArrayAdapter<String> gradesArrayAdapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_spinner_item, gradesArray );
            semesterGradeSpinner.setAdapter(gradesArrayAdapter);

            int gradeIndex = Arrays.binarySearch(institution.getGrades(), course.getGrade());

            semesterGradeSpinner.setSelection(gradeIndex);
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
        private ArrayList<Course> courses;

        public CourseAdapter(ArrayList<Course> courses) {
            this.courses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CourseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            Course currentCourse = courses.get(position);
            holder.bind(currentCourse);
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }

        public void setCourses(ArrayList<Course> courses) {
            this.courses = courses;
        }
    }
}   //  end of class
