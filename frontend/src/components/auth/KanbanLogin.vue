<template>
  <div class="kanban-login">
    <form name='f' @submit.prevent="login">
      <div class="login-form-container">
        <div class="form-color form-inner"><h1>Login</h1></div>
        <div class="form-inner">
          <input class="login-input-field" type="text" placeholder="Enter Username" name="uname"
                 v-model="username" required><br>
          <input class="login-input-field" type="password" placeholder="Enter Password" name="psw"
                 v-model="password" required><br>
          <div class="inner-body-footer">
            <div class="form-checkbox">
              <label id="label-checkbox">
                <input class="check" type="checkbox" name="remember"> Remember me
              </label>
            </div>
            <div class="form-button">
              <button id="login-button" type="submit">Login</button>
            </div>
          </div>
        </div>

        <div class="form-inner form-color">
          <span class="psw"><a href="#">Forgot password?</a></span>
        </div>
        <div v-if="error" style="color: red;">{{ error }}</div>
      </div>
    </form>
  </div>
</template>
<script>
export default {
  name: 'Login',
  data () {
    return {
      username: '',
      password: '',
      error: false
    }
  },
  methods: {
    login () {
      const { username, password } = this
      this.$store.dispatch('AUTHENTICATE', { username, password })
        .then(() => { this.$router.push('/board') })
        .catch(e => {
          this.error = 'Login failed! ' + e.response.data
        })
    }
  }
}
</script>
<style scoped>
  .form-color {
    background: #aaffaa !important;
  }
  .login-input-field {
    font-family: "Times New Roman", Times, serif;
    font-size: large;
    width: 80%;
    outline: none;
    margin: 15px 0;
    border: none;
    border-bottom: 2px solid black;
  }
  .form-inner {
    background: white;
    text-align: center;
    padding: 10px;
  }
  .inner-body-footer {
    display: flex;
    flex-direction: row;
    width: 80%;
    margin-left: 10%;
    height: 30px;
    background: white;
    text-align: center;
  }
  .login-form-container {
    margin: 100px auto;
    width:  400px;
    box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
  }
  #login-button {
    margin-right: 10%;
    width: 100%;
    height: 30px;
    background: white;
    color: black;
    border: 2px solid black;
    border-radius: 2px;
    outline: none;
    -webkit-transition-duration: 0.4s; /* Safari */
    transition-duration: 0.4s;
  }
  #login-button:hover {
    background-color: #aaffaa;
    border: 2px solid rgba(0, 0, 0, 0);
  }
  .form-checkbox {
    padding-top: 3px;
    order: 1;
    flex-grow: 1;
  }
  .form-button {
    order: 2;
    flex-grow: 4;
  }
</style>
