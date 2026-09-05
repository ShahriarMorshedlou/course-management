// آدرس پایه بک‌اند - اگه پورت یا context-path متفاوته، همینجا عوض کنید
const BASE_URL = 'http://localhost:8080';

const errorBox = document.getElementById('errorBox');
const formErrorBox = document.getElementById('formErrorBox');
const saveStudentBtn = document.getElementById('saveStudentBtn');
const addStudentBtn = document.getElementById('addStudentBtn');
const studentModalEl = document.getElementById('studentModal');
const studentModalLabel = document.getElementById('studentModalLabel');
const studentModal = new bootstrap.Modal(studentModalEl);
const studentsTableBody = document.getElementById('studentsTableBody');
const searchInput = document.getElementById('searchInput');

// null یعنی حالت Create؛ یک id یعنی حالت Update برای همون دانشجو
let editingStudentId = null;

// متغیرهای مربوط به Pagination
let currentPage = 0;
const pageSize = 10;

addStudentBtn.addEventListener('click', () => {
  editingStudentId = null;
  studentModalLabel.textContent = 'افزودن دانشجو';
  document.getElementById('studentForm').reset();
  hideFormError();
});

saveStudentBtn.addEventListener('click', () => {
  if (editingStudentId === null) {
    createStudent();
  } else {
    updateStudent(editingStudentId);
  }
});

// دکمه‌های صفحه‌بندی
document.getElementById('prevPageBtn').addEventListener('click', () => {
  currentPage--;
  loadStudents();
});

document.getElementById('nextPageBtn').addEventListener('click', () => {
  currentPage++;
  loadStudents();
});

document.addEventListener('DOMContentLoaded', () => {
  loadStudents();
});

// جستجوی زنده بر اساس firstName - توجه: اسم Query Param اینجا "query" هست، نه "title"/"specialty"
searchInput.addEventListener('input', () => {
  const query = searchInput.value.trim();
  if (query === '') {
    loadStudents();
  } else {
    searchStudents(query);
  }
});

// Event Delegation برای دکمه‌های ویرایش/حذف
studentsTableBody.addEventListener('click', (event) => {
  if (event.target.classList.contains('btn-edit')) {
    const id = event.target.dataset.id;
    openEditModal(id);
  }

  if (event.target.classList.contains('btn-delete')) {
    const id = event.target.dataset.id;
    const isConfirmed = confirm('آیا از حذف این دانشجو مطمئن هستید؟');
    if (isConfirmed) {
      deleteStudent(id);
    }
  }
});

// ---------- GET /student?page=..&size=.. : گرفتن یک صفحه از دانشجویان ----------
function loadStudents() {
  hideListError();

  fetch(`${BASE_URL}/student?page=${currentPage}&size=${pageSize}`, { method: 'GET' })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((pageData) => {
      renderStudents(pageData.content);
      renderPagination(pageData);
    })
    .catch((error) => {
      showListError(error.message);
    });
}

function renderPagination(pageData) {
  document.getElementById('pageInfo').textContent =
    `صفحه ${pageData.number + 1} از ${pageData.totalPages} (مجموع ${pageData.totalElements} دانشجو)`;

  document.getElementById('prevPageBtn').disabled = pageData.first;
  document.getElementById('nextPageBtn').disabled = pageData.last;
}

// ---------- GET /student/search?query=... : جستجو بر اساس firstName ----------
function searchStudents(query) {
  hideListError();

  fetch(`${BASE_URL}/student/search?query=${encodeURIComponent(query)}`, { method: 'GET' })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((students) => {
      renderStudents(students);
    })
    .catch((error) => {
      showListError(error.message);
    });
}

// فقط ساخت UI جدول - هیچ ارتباطی با شبکه نداره
function renderStudents(students) {
  studentsTableBody.innerHTML = '';

  if (students.length === 0) {
    studentsTableBody.innerHTML = `
      <tr><td colspan="5" class="text-center text-muted">هیچ دانشجویی ثبت نشده</td></tr>
    `;
    return;
  }

  students.forEach((student) => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${student.id}</td>
      <td>${student.firstName}</td>
      <td>${student.lastName}</td>
      <td>${student.email}</td>
      <td>
        <button class="btn btn-sm btn-warning btn-edit" data-id="${student.id}">ویرایش</button>
        <button class="btn btn-sm btn-danger btn-delete" data-id="${student.id}">حذف</button>
        <button class="btn btn-sm btn-info btn-view-courses" data-id="${student.id}">دوره‌ها</button>
      </td>
    `;
    studentsTableBody.appendChild(row);
  });
}

// ---------- POST /student : ساخت دانشجوی جدید ----------
function createStudent() {
  const requestBody = {
    firstName: document.getElementById('firstName').value,
    lastName: document.getElementById('lastName').value,
    email: document.getElementById('email').value
  };

  hideFormError();

  fetch(`${BASE_URL}/student`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(requestBody)
  })
    .then(async (response) => {
      if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData ? JSON.stringify(errorData) : `خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((createdStudent) => {
      console.log('دانشجو ساخته شد:', createdStudent);
      studentModal.hide();
      document.getElementById('studentForm').reset();
      currentPage = 0;
      loadStudents();
    })
    .catch((error) => {
      showFormError(error.message);
    });
}

// ---------- GET /student/{id} : گرفتن اطلاعات یک دانشجو برای پر کردن فرم ویرایش ----------
function openEditModal(id) {
  hideFormError();

  fetch(`${BASE_URL}/student/${id}`, { method: 'GET' })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((student) => {
      document.getElementById('firstName').value = student.firstName;
      document.getElementById('lastName').value = student.lastName;
      document.getElementById('email').value = student.email;

      editingStudentId = student.id;
      studentModalLabel.textContent = 'ویرایش دانشجو';
      studentModal.show();
    })
    .catch((error) => {
      showListError(error.message);
    });
}

// ---------- PUT /student/{id} : ثبت تغییرات یک دانشجوی موجود ----------
function updateStudent(id) {
  const requestBody = {
    firstName: document.getElementById('firstName').value,
    lastName: document.getElementById('lastName').value,
    email: document.getElementById('email').value
  };

  hideFormError();

  fetch(`${BASE_URL}/student/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(requestBody)
  })
    .then(async (response) => {
      if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData ? JSON.stringify(errorData) : `خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((updatedStudent) => {
      console.log('دانشجو ویرایش شد:', updatedStudent);
      studentModal.hide();
      document.getElementById('studentForm').reset();
      editingStudentId = null;
      loadStudents();
    })
    .catch((error) => {
      showFormError(error.message);
    });
}

// ---------- DELETE /student/{id} : حذف یک دانشجو ----------
function deleteStudent(id) {
  hideListError();

  fetch(`${BASE_URL}/student/${id}`, { method: 'DELETE' })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      loadStudents();
    })
    .catch((error) => {
      showListError(error.message);
    });
}

function showFormError(message) {
  formErrorBox.textContent = message;
  formErrorBox.classList.remove('d-none');
}

function hideFormError() {
  formErrorBox.classList.add('d-none');
  formErrorBox.textContent = '';
}

function showListError(message) {
  errorBox.textContent = message;
  errorBox.classList.remove('d-none');
}

function hideListError() {
  errorBox.classList.add('d-none');
  errorBox.textContent = '';
}