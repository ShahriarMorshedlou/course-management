// آدرس پایه بک‌اند - اگه پورت یا context-path متفاوته، همینجا عوض کنید
const BASE_URL = 'http://localhost:8080';

const errorBox = document.getElementById('errorBox');
const formErrorBox = document.getElementById('formErrorBox');
const saveTeacherBtn = document.getElementById('saveTeacherBtn');
const addTeacherBtn = document.getElementById('addTeacherBtn');
const teacherModalEl = document.getElementById('teacherModal');
const teacherModalLabel = document.getElementById('teacherModalLabel');
const teacherModal = new bootstrap.Modal(teacherModalEl);
const teachersTableBody = document.getElementById('teachersTableBody');
const searchInput = document.getElementById('searchInput');

// null یعنی حالت Create؛ یک id یعنی حالت Update برای همون استاد
let editingTeacherId = null;

// متغیرهای مربوط به Pagination
let currentPage = 0;
const pageSize = 10;

addTeacherBtn.addEventListener('click', () => {
  editingTeacherId = null;
  teacherModalLabel.textContent = 'افزودن استاد';
  document.getElementById('teacherForm').reset();
  hideFormError();
});

saveTeacherBtn.addEventListener('click', () => {
  if (editingTeacherId === null) {
    createTeacher();
  } else {
    updateTeacher(editingTeacherId);
  }
});

// دکمه‌های صفحه‌بندی
document.getElementById('prevPageBtn').addEventListener('click', () => {
  currentPage--;
  loadTeachers();
});

document.getElementById('nextPageBtn').addEventListener('click', () => {
  currentPage++;
  loadTeachers();
});

document.addEventListener('DOMContentLoaded', () => {
  loadTeachers();
});

// جستجوی زنده بر اساس specialty
searchInput.addEventListener('input', () => {
  const specialty = searchInput.value.trim();
  if (specialty === '') {
    loadTeachers();
  } else {
    searchTeachers(specialty);
  }
});

// Event Delegation برای دکمه‌های ویرایش/حذف که بعداً داخل جدول ساخته می‌شن
teachersTableBody.addEventListener('click', (event) => {
  if (event.target.classList.contains('btn-edit')) {
    const id = event.target.dataset.id;
    openEditModal(id);
  }

  if (event.target.classList.contains('btn-delete')) {
    const id = event.target.dataset.id;
    const isConfirmed = confirm('آیا از حذف این استاد مطمئن هستید؟');
    if (isConfirmed) {
      deleteTeacher(id);
    }
  }
});

// ---------- GET /teacher?page=..&size=.. : گرفتن یک صفحه از اساتید ----------
function loadTeachers() {
  hideListError();

  fetch(`${BASE_URL}/teacher?page=${currentPage}&size=${pageSize}`, { method: 'GET' })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((pageData) => {
      renderTeachers(pageData.content);
      renderPagination(pageData);
    })
    .catch((error) => {
      showListError(error.message);
    });
}

function renderPagination(pageData) {
  document.getElementById('pageInfo').textContent =
    `صفحه ${pageData.number + 1} از ${pageData.totalPages} (مجموع ${pageData.totalElements} استاد)`;

  document.getElementById('prevPageBtn').disabled = pageData.first;
  document.getElementById('nextPageBtn').disabled = pageData.last;
}

// ---------- GET /teacher/search?specialty=... : جستجو ----------
function searchTeachers(specialty) {
  hideListError();

  fetch(`${BASE_URL}/teacher/search?specialty=${encodeURIComponent(specialty)}`, { method: 'GET' })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((teachers) => {
      renderTeachers(teachers);
    })
    .catch((error) => {
      showListError(error.message);
    });
}

// فقط ساخت UI جدول بر اساس آرایه‌ای که گرفتیم - هیچ ارتباطی با شبکه نداره
function renderTeachers(teachers) {
  teachersTableBody.innerHTML = '';

  if (teachers.length === 0) {
    teachersTableBody.innerHTML = `
      <tr><td colspan="6" class="text-center text-muted">هیچ استادی ثبت نشده</td></tr>
    `;
    return;
  }

  teachers.forEach((teacher) => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${teacher.id}</td>
      <td>${teacher.firstName}</td>
      <td>${teacher.lastName}</td>
      <td>${teacher.email}</td>
      <td>${teacher.specialty}</td>
      <td>
        <button class="btn btn-sm btn-warning btn-edit" data-id="${teacher.id}">ویرایش</button>
        <button class="btn btn-sm btn-danger btn-delete" data-id="${teacher.id}">حذف</button>
        <button class="btn btn-sm btn-info btn-view-courses" data-id="${teacher.id}">دوره‌ها</button>
      </td>
    `;
    teachersTableBody.appendChild(row);
  });
}

// ---------- POST /teacher : ساخت استاد جدید ----------
function createTeacher() {
  const requestBody = {
    firstName: document.getElementById('firstName').value,
    lastName: document.getElementById('lastName').value,
    email: document.getElementById('email').value,
    specialty: document.getElementById('specialty').value
  };

  hideFormError();

  fetch(`${BASE_URL}/teacher`, {
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
    .then((createdTeacher) => {
      console.log('استاد ساخته شد:', createdTeacher);
      teacherModal.hide();
      document.getElementById('teacherForm').reset();
      currentPage = 0;
      loadTeachers();
    })
    .catch((error) => {
      showFormError(error.message);
    });
}

// ---------- GET /teacher/{id} : گرفتن اطلاعات یک استاد برای پر کردن فرم ویرایش ----------
function openEditModal(id) {
  hideFormError();

  fetch(`${BASE_URL}/teacher/${id}`, { method: 'GET' })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((teacher) => {
      document.getElementById('firstName').value = teacher.firstName;
      document.getElementById('lastName').value = teacher.lastName;
      document.getElementById('email').value = teacher.email;
      document.getElementById('specialty').value = teacher.specialty;

      editingTeacherId = teacher.id;
      teacherModalLabel.textContent = 'ویرایش استاد';
      teacherModal.show();
    })
    .catch((error) => {
      showListError(error.message);
    });
}

// ---------- PUT /teacher/{id} : ثبت تغییرات یک استاد موجود ----------
function updateTeacher(id) {
  // برخلاف Course، اینجا همه فیلدها در Update هم ارسال می‌شن (چون TeacherRequest برای هر دو حالت یکیه)
  const requestBody = {
    firstName: document.getElementById('firstName').value,
    lastName: document.getElementById('lastName').value,
    email: document.getElementById('email').value,
    specialty: document.getElementById('specialty').value
  };

  hideFormError();

  fetch(`${BASE_URL}/teacher/${id}`, {
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
    .then((updatedTeacher) => {
      console.log('استاد ویرایش شد:', updatedTeacher);
      teacherModal.hide();
      document.getElementById('teacherForm').reset();
      editingTeacherId = null;
      loadTeachers();
    })
    .catch((error) => {
      showFormError(error.message);
    });
}

// ---------- DELETE /teacher/{id} : حذف یک استاد ----------
function deleteTeacher(id) {
  hideListError();

  fetch(`${BASE_URL}/teacher/${id}`, { method: 'DELETE' })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      loadTeachers();
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