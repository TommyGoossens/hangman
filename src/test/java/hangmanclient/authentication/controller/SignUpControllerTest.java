package hangmanclient.authentication.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SignUpControllerTest {
    private Dataset pwDataset1 = new Dataset("","");
    private Dataset pwDataset2 = new Dataset("12345","12345");
    private Dataset pwDataset3 = new Dataset("1234567891011121314151617","1234567891011121314151617");
    private Dataset pwDataset4 = new Dataset("123456","123456");
    private Dataset pwDataset5 = new Dataset("123456","pizzapunt");

    private ISignUp controller;

    @Before
    public void setUp(){
        controller = new SignUpController();
    }

    @Test
    public void passWordIsEmpty() {
        assertFalse(controller.passwordMeetsRequirements(pwDataset1.password1,pwDataset1.password2));
    }

    @Test
    public void passWordIsTooShort(){
        assertFalse(controller.passwordMeetsRequirements(pwDataset2.password1,pwDataset2.password2));
    }

    @Test
    public void passWordIsTooLong(){
        assertFalse(controller.passwordMeetsRequirements(pwDataset3.password1,pwDataset3.password2));
    }
    @Test
    public void passWordMeetsRequirements(){
        assertTrue(controller.passwordMeetsRequirements(pwDataset4.password1,pwDataset4.password2));
    }
    @Test
    public void passWordDoNotMatch(){
        assertFalse(controller.passwordMeetsRequirements(pwDataset5.password1,pwDataset5.password2));
    }

    private class Dataset{
        String password1;
        String password2;

        public Dataset(String password1, String password2) {
            this.password1 = password1;
            this.password2 = password2;
        }
    }
}

