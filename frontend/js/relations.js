// این فایل مستقل، Featureهای مربوط به رابطه‌ی بین Course/Teacher/Student رو مدیریت می‌کنه.
// به BASE_URL و عناصر جدول (coursesTableBody/teachersTableBody/studentsTableBody) که در
// فایل‌های courses.js/teachers.js/students.js تعریف شدن، مستقیم دسترسی داره (چون هر دو در یک
// فضای مشترک/Global اجرا می‌شن)، به شرطی که این فایل در HTML بعد از اون فایل‌ها لود بشه.

// ---------- توابع کمکی مشترک ----------

// یک Dropdown رو با داده‌ای که از سرور گرفتیم پر می‌کنه
function populateDropdown(selectEl, items, getLabel) {
  selectEl.innerHTML = '';
  items.forEach((item) => {
    const option = document.createElement('option');
    option.value = item.id;
    option.textContent = getLabel(item);
    selectEl.appendChild(option);
  });
}

// چون لیست‌های Teacher/Student الان Pagination دارن، برای پر کردن Dropdown
// یک صفحه‌ی بزرگ (size=1000) می‌گیریم تا عملاً "همه" رو داشته باشیم
function fetchAllForDropdown(path) {
  return fetch(`${BASE_URL}${path}?page=0&size=1000`)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((pageData) => pageData.content);
}

// ==================================================================
// بخش مربوط به courses.html: مشاهده/ثبت‌نام دانشجو + اختصاص استاد
// ==================================================================
if (typeof coursesTableBody !== 'undefined') {

  const courseStudentsModal = new bootstrap.Modal(document.getElementById('courseStudentsModal'));
  const assignTeacherModal = new bootstrap.Modal(document.getElementById('assignTeacherModal'));
  const enrolledStudentsList = document.getElementById('enrolledStudentsList');
  const studentSelectDropdown = document.getElementById('studentSelectDropdown');
  const teacherSelectDropdown = document.getElementById('teacherSelectDropdown');
  const enrollStudentBtn = document.getElementById('enrollStudentBtn');
  const assignTeacherBtn = document.getElementById('assignTeacherBtn');
  const enrollErrorBox = document.getElementById('enrollErrorBox');
  const assignErrorBox = document.getElementById('assignErrorBox');

  // به یاد داشتن اینکه الان داریم برای کدوم دوره کار می‌کنیم
  let activeCourseId = null;

  // کلیک روی دکمه‌های جدید در جدول دوره‌ها
  coursesTableBody.addEventListener('click', (event) => {
    if (event.target.classList.contains('btn-view-students')) {
      activeCourseId = event.target.dataset.id;
      openCourseStudentsModal(activeCourseId);
    }

    if (event.target.classList.contains('btn-assign-teacher')) {
      activeCourseId = event.target.dataset.id;
      openAssignTeacherModal(activeCourseId);
    }
  });

  // ---------- GET /course/{id}/students : نمایش دانشجویان ثبت‌نام‌شده ----------
  function loadEnrolledStudents(courseId) {
    fetch(`${BASE_URL}/course/${courseId}/students`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`خطای سرور: ${response.status}`);
        }
        return response.json();
      })
      .then((students) => {
        enrolledStudentsList.innerHTML = '';
        if (students.length === 0) {
          enrolledStudentsList.innerHTML = '<li class="list-group-item text-muted">هیچ دانشجویی ثبت‌نام نکرده</li>';
          return;
        }
        students.forEach((student) => {
          const li = document.createElement('li');
          li.className = 'list-group-item';
          li.textContent = `${student.firstName} ${student.lastName}`;
          enrolledStudentsList.appendChild(li);
        });
      })
      .catch((error) => {
        enrollErrorBox.textContent = error.message;
        enrollErrorBox.classList.remove('d-none');
      });
  }

  function openCourseStudentsModal(courseId) {
    enrollErrorBox.classList.add('d-none');
    loadEnrolledStudents(courseId);

    // Dropdown رو با لیست همه دانشجویان پر می‌کنیم
    fetchAllForDropdown('/student')
      .then((students) => {
        populateDropdown(studentSelectDropdown, students, (s) => `${s.firstName} ${s.lastName}`);
      });

    courseStudentsModal.show();
  }

  // ---------- POST /course/{courseId}/{studentId} : ثبت‌نام دانشجوی انتخاب‌شده ----------
  enrollStudentBtn.addEventListener('click', () => {
    const studentId = studentSelectDropdown.value;

    fetch(`${BASE_URL}/course/${activeCourseId}/${studentId}`, { method: 'POST' })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`خطای سرور: ${response.status}`);
        }
        // لیست دانشجویان همین Modal رو رفرش می‌کنیم تا دانشجوی تازه‌ثبت‌نام‌شده دیده بشه
        loadEnrolledStudents(activeCourseId);
      })
      .catch((error) => {
        enrollErrorBox.textContent = error.message;
        enrollErrorBox.classList.remove('d-none');
      });
  });

  // ---------- باز کردن Modal اختصاص استاد ----------
  function openAssignTeacherModal(courseId) {
    assignErrorBox.classList.add('d-none');

    fetchAllForDropdown('/teacher')
      .then((teachers) => {
        populateDropdown(teacherSelectDropdown, teachers, (t) => `${t.firstName} ${t.lastName}`);
      });

    assignTeacherModal.show();
  }

  // ---------- POST /course/{courseId}/teacher/{teacherId} : ثبت استاد انتخاب‌شده ----------
  assignTeacherBtn.addEventListener('click', () => {
    const teacherId = teacherSelectDropdown.value;

    fetch(`${BASE_URL}/course/${activeCourseId}/teacher/${teacherId}`, { method: 'POST' })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`خطای سرور: ${response.status}`);
        }
        assignTeacherModal.hide();
        loadCourses(); // تابعی از courses.js - جدول اصلی رو رفرش می‌کنه تا ستون "استاد" آپدیت بشه
      })
      .catch((error) => {
        assignErrorBox.textContent = error.message;
        assignErrorBox.classList.remove('d-none');
      });
  });
}

// ==================================================================
// بخش مربوط به teachers.html: مشاهده دوره‌های یک استاد (فقط نمایش)
// ==================================================================
if (typeof teachersTableBody !== 'undefined') {

  const relatedCoursesModal = new bootstrap.Modal(document.getElementById('relatedCoursesModal'));
  const relatedCoursesList = document.getElementById('relatedCoursesList');
  const relatedCoursesModalLabel = document.getElementById('relatedCoursesModalLabel');

  teachersTableBody.addEventListener('click', (event) => {
    if (event.target.classList.contains('btn-view-courses')) {
      const teacherId = event.target.dataset.id;
      openTeacherCoursesModal(teacherId);
    }
  });

  // ---------- GET /teacher/{id}/courses ----------
  function openTeacherCoursesModal(teacherId) {
    relatedCoursesModalLabel.textContent = 'دوره‌های این استاد';

    fetch(`${BASE_URL}/teacher/${teacherId}/courses`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`خطای سرور: ${response.status}`);
        }
        return response.json();
      })
      .then((courses) => {
        renderRelatedCoursesList(courses);
        relatedCoursesModal.show();
      });
  }

  function renderRelatedCoursesList(courses) {
    relatedCoursesList.innerHTML = '';
    if (courses.length === 0) {
      relatedCoursesList.innerHTML = '<li class="list-group-item text-muted">دوره‌ای ثبت نشده</li>';
      return;
    }
    courses.forEach((course) => {
      const li = document.createElement('li');
      li.className = 'list-group-item';
      li.textContent = `${course.title} (${course.code})`;
      relatedCoursesList.appendChild(li);
    });
  }
}

// ==================================================================
// بخش مربوط به students.html: مشاهده دوره‌های یک دانشجو (فقط نمایش)
// ==================================================================
if (typeof studentsTableBody !== 'undefined') {

  const relatedCoursesModal = new bootstrap.Modal(document.getElementById('relatedCoursesModal'));
  const relatedCoursesList = document.getElementById('relatedCoursesList');
  const relatedCoursesModalLabel = document.getElementById('relatedCoursesModalLabel');

  studentsTableBody.addEventListener('click', (event) => {
    if (event.target.classList.contains('btn-view-courses')) {
      const studentId = event.target.dataset.id;
      openStudentCoursesModal(studentId);
    }
  });

  // ---------- GET /student/{id}/courses ----------
  function openStudentCoursesModal(studentId) {
    relatedCoursesModalLabel.textContent = 'دوره‌های این دانشجو';

    fetch(`${BASE_URL}/student/${studentId}/courses`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`خطای سرور: ${response.status}`);
        }
        return response.json();
      })
      .then((courses) => {
        renderRelatedCoursesList(courses);
        relatedCoursesModal.show();
      });
  }

  function renderRelatedCoursesList(courses) {
    relatedCoursesList.innerHTML = '';
    if (courses.length === 0) {
      relatedCoursesList.innerHTML = '<li class="list-group-item text-muted">دوره‌ای ثبت نشده</li>';
      return;
    }
    courses.forEach((course) => {
      const li = document.createElement('li');
      li.className = 'list-group-item';
      li.textContent = `${course.title} (${course.code})`;
      relatedCoursesList.appendChild(li);
    });
  }
}