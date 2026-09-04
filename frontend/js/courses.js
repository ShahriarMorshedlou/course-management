// آدرس پایه بک‌اند - اگه پورت یا context-path متفاوته، همینجا عوض کنید
const BASE_URL = 'http://localhost:8080';

const errorBox = document.getElementById('errorBox');
const formErrorBox = document.getElementById('formErrorBox');
const saveCourseBtn = document.getElementById('saveCourseBtn');
const addCourseBtn = document.getElementById('addCourseBtn');
const courseModalEl = document.getElementById('courseModal');
const courseModalLabel = document.getElementById('courseModalLabel');
const courseModal = new bootstrap.Modal(courseModalEl);
const coursesTableBody = document.getElementById('coursesTableBody');
const codeInput = document.getElementById('code');
const searchInput = document.getElementById('searchInput');

// این متغیر «حافظه‌ی حالت فعلی Modal» هست:
// اگه null باشه یعنی داریم دوره‌ی جدید می‌سازیم (Create)
// اگه یک عدد باشه (id یک دوره) یعنی داریم همون دوره رو ویرایش می‌کنیم (Update)
let editingCourseId = null;

// متغیرهای مربوط به Pagination
let currentPage = 0;
const pageSize = 10;

// هر بار که کاربر داخل فیلد جستجو چیزی تایپ می‌کنه، این کد اجرا می‌شه
searchInput.addEventListener('input', () => {
  const title = searchInput.value.trim(); // trim() یعنی فاصله‌های اضافه‌ی ابتدا/انتها رو حذف کن

  if (title === '') {
    loadCourses(); // اگه فیلد خالی شد، برگرد به حالت "نمایش همه"
  } else {
    searchCourses(title);
  }
});

// وقتی دکمه "افزودن دوره" کلیک می‌شه، مطمئن می‌شیم فرم در حالت Create ریست شده
addCourseBtn.addEventListener('click', () => {
  editingCourseId = null;
  courseModalLabel.textContent = 'افزودن دوره';
  document.getElementById('courseForm').reset();
  codeInput.disabled = false;
  hideFormError();
});

// وقتی دکمه "ذخیره" داخل Modal کلیک می‌شه
// بسته به اینکه editingCourseId مقدار داره یا نه، Update یا Create صدا زده می‌شه
saveCourseBtn.addEventListener('click', () => {
  if (editingCourseId === null) {
    createCourse();
  } else {
    updateCourse(editingCourseId);
  }
});

// دکمه‌های صفحه‌بندی
document.getElementById('prevPageBtn').addEventListener('click', () => {
  currentPage--;
  loadCourses();
});

document.getElementById('nextPageBtn').addEventListener('click', () => {
  currentPage++;
  loadCourses();
});

// وقتی خود صفحه کامل لود شد، لیست دوره‌ها رو از API بگیر
document.addEventListener('DOMContentLoaded', () => {
  loadCourses();
});

// Event Delegation: به‌جای گذاشتن Listener روی هر دکمه‌ی "ویرایش" (که در ابتدا وجود نداره، بعداً با JS ساخته می‌شه)،
// یک Listener روی خود جدول می‌ذاریم و چک می‌کنیم کدوم دکمه کلیک شده
coursesTableBody.addEventListener('click', (event) => {
  if (event.target.classList.contains('btn-edit')) {
    const id = event.target.dataset.id; // مقدار data-id که در renderCourses ست کردیم
    openEditModal(id);
  }

  if (event.target.classList.contains('btn-delete')) {
    const id = event.target.dataset.id;
    // confirm یک Dialog ساده و بومی مرورگره؛ اگه کاربر "Cancel" بزنه، false برمی‌گرده و کد ادامه پیدا نمی‌کنه
    const isConfirmed = confirm('آیا از حذف این دوره مطمئن هستید؟');
    if (isConfirmed) {
      deleteCourse(id);
    }
  }
});

// ---------- GET /course?page=..&size=.. : گرفتن یک صفحه از دوره‌ها ----------
function loadCourses() {
  hideListError();

  fetch(`${BASE_URL}/course?page=${currentPage}&size=${pageSize}`, {
    method: 'GET'
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      return response.json(); // الان یک Object برمی‌گرده: {content: [...], totalPages, totalElements, number, first, last}
    })
    .then((pageData) => {
      renderCourses(pageData.content); // آرایه‌ی واقعی دوره‌ها داخل content هست
      renderPagination(pageData);
    })
    .catch((error) => {
      showListError(error.message);
    });
}

// این تابع فقط اطلاعات صفحه‌بندی (متن "صفحه X از Y" و فعال/غیرفعال بودن دکمه‌ها) رو آپدیت می‌کنه
function renderPagination(pageData) {
  document.getElementById('pageInfo').textContent =
    `صفحه ${pageData.number + 1} از ${pageData.totalPages} (مجموع ${pageData.totalElements} دوره)`;

  document.getElementById('prevPageBtn').disabled = pageData.first;
  document.getElementById('nextPageBtn').disabled = pageData.last;
}

// این تابع فقط UI رو بر اساس آرایه‌ای که از API گرفتیم می‌سازه (هیچ ارتباطی با شبکه نداره)
function renderCourses(courses) {
  coursesTableBody.innerHTML = ''; // اول جدول رو خالی می‌کنیم

  if (courses.length === 0) {
    coursesTableBody.innerHTML = `
      <tr><td colspan="7" class="text-center text-muted">هیچ دوره‌ای ثبت نشده</td></tr>
    `;
    return;
  }

  courses.forEach((course) => {
    // teacher ممکنه null باشه (چون Create فعلاً teacherId نمی‌گیره)
    // Optional Chaining (?.) یعنی: اگه course.teacher وجود نداشت (null/undefined)، بقیه رو نخون و undefined برگردون، به‌جای اینکه Error بده
    const teacherName = course.teacher
      ? `${course.teacher.firstName} ${course.teacher.lastName}`
      : '<span class="text-muted">تعیین نشده</span>';

    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${course.id}</td>
      <td>${course.title}</td>
      <td>${course.code}</td>
      <td>${course.startDate}</td>
      <td>${course.endDate}</td>
      <td>${teacherName}</td>
      <td>
        <button class="btn btn-sm btn-warning btn-edit" data-id="${course.id}">ویرایش</button>
        <button class="btn btn-sm btn-danger btn-delete" data-id="${course.id}">حذف</button>
      </td>
    `;
    coursesTableBody.appendChild(row);
  });
}

// ---------- GET /course/{id} : گرفتن اطلاعات یک دوره برای پر کردن فرم ویرایش ----------
function openEditModal(id) {
  hideFormError();

  fetch(`${BASE_URL}/course/${id}`, {
    method: 'GET'
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      return response.json(); // یک CourseResponse تکی برمی‌گرده: {id, title, code, startDate, endDate, teacher}
    })
    .then((course) => {
      // فرم رو با مقادیر فعلی همون دوره پر می‌کنیم
      document.getElementById('title').value = course.title;
      document.getElementById('code').value = course.code;
      document.getElementById('startDate').value = course.startDate;
      document.getElementById('endDate').value = course.endDate;

      // چون UpdateCourseRequest فیلد code نداره، این فیلد رو در حالت ویرایش غیرفعال می‌کنیم
      codeInput.disabled = true;

      editingCourseId = course.id; // از این لحظه، Modal در حالت "ویرایش" هست
      courseModalLabel.textContent = 'ویرایش دوره';
      courseModal.show();
    })
    .catch((error) => {
      showListError(error.message); // چون این خطا مربوط به کل صفحه‌ست (قبل از باز شدن Modal)، در errorBox عمومی نشون می‌دیم
    });
}

// ---------- PUT /course/{id} : ثبت تغییرات یک دوره‌ی موجود ----------
function updateCourse(id) {
  // توجه: code اینجا نیست، چون UpdateCourseRequest همچین فیلدی نداره
  const requestBody = {
    title: document.getElementById('title').value,
    startDate: document.getElementById('startDate').value,
    endDate: document.getElementById('endDate').value
  };

  hideFormError();

  fetch(`${BASE_URL}/course/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(requestBody)
  })
    .then(async (response) => {
      if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData ? JSON.stringify(errorData) : `خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((updatedCourse) => {
      console.log('دوره ویرایش شد:', updatedCourse);
      courseModal.hide();
      document.getElementById('courseForm').reset();
      codeInput.disabled = false;
      editingCourseId = null; // برگشت به حالت Create برای دفعه بعد
      loadCourses();
    })
    .catch((error) => {
      showFormError(error.message);
    });
}

// ---------- DELETE /course/{id} : حذف یک دوره ----------
function deleteCourse(id) {
  hideListError();

  fetch(`${BASE_URL}/course/${id}`, {
    method: 'DELETE'
  })
    .then((response) => {
      // توجه: چون Backend اینجا 204 (No Content) برمی‌گردونه، هیچ Bodyای برای پارس کردن نیست
      // پس اینجا response.json() صدا نمی‌زنیم، فقط چک می‌کنیم موفق بوده یا نه
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      loadCourses(); // جدول رو دوباره از سرور می‌گیریم تا رکورد حذف‌شده دیگه نباشه
    })
    .catch((error) => {
      showListError(error.message);
    });
}

// ---------- GET /course/search?title=... : جستجوی دوره بر اساس عنوان ----------
function searchCourses(title) {
  hideListError();

  // encodeURIComponent مقدار title رو برای استفاده امن در URL آماده می‌کنه
  // (مثلاً اگه کاربر فاصله یا کاراکتر خاص تایپ کنه، به فرمت درست تبدیل می‌شه)
  fetch(`${BASE_URL}/course/search?title=${encodeURIComponent(title)}`, {
    method: 'GET'
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`خطای سرور: ${response.status}`);
      }
      return response.json();
    })
    .then((courses) => {
      renderCourses(courses); // همون تابع رندر قبلی رو دوباره استفاده می‌کنیم، چون خروجی دقیقاً همون شکل CourseResponse[] رو داره
    })
    .catch((error) => {
      showListError(error.message);
    });
}

function showListError(message) {
  errorBox.textContent = message;
  errorBox.classList.remove('d-none');
}

function hideListError() {
  errorBox.classList.add('d-none');
  errorBox.textContent = '';
}

function createCourse() {
  // 1) مقادیر فرم رو می‌خونیم و یک Object می‌سازیم
  //    نام کلیدها باید دقیقاً با فیلدهای CreateCourseRequest یکی باشه
  const requestBody = {
    title: document.getElementById('title').value,
    code: document.getElementById('code').value,
    startDate: document.getElementById('startDate').value, // فرمت yyyy-MM-dd همون چیزیه که LocalDate می‌خواد
    endDate: document.getElementById('endDate').value
  };

  hideFormError();

  // 2) درخواست POST رو می‌فرستیم
  fetch(`${BASE_URL}/course`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(requestBody)
  })
    .then(async (response) => {
      // response.ok یعنی status بین 200 تا 299 هست (پس 201 هم جزوشه)
      if (!response.ok) {
        // خطا رو از بدنه پاسخ می‌خونیم تا به کاربر نشون بدیم
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData ? JSON.stringify(errorData) : `خطای سرور: ${response.status}`);
      }
      return response.json(); // این خط، بدنه JSON رو تبدیل به Object جاوااسکریپتی می‌کنه (CourseResponse)
    })
    .then((createdCourse) => {
      // createdCourse دقیقاً شکل CourseResponse رو داره: { id, title, code, startDate, endDate }
      console.log('دوره ساخته شد:', createdCourse);
      courseModal.hide();
      document.getElementById('courseForm').reset();
      currentPage = 0; // برگرد به صفحه اول تا دوره تازه ساخته‌شده دیده بشه
      loadCourses(); // جدول رو دوباره از سرور می‌گیریم تا رکورد جدید هم توش باشه
    })
    .catch((error) => {
      // خطاهای Validation (400) یا خطاهای شبکه/CORS اینجا می‌افتن
      showFormError(error.message);
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