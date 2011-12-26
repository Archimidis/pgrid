/*
 * This file (client.Main) is part of the libpgrid project.
 *
 * Copyright (c) 2011. Vourlakis Nikolas. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package client;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MainModule());
        PaymentFactory factory = injector.getInstance(PaymentFactory.class);
        Payment payment = factory.create("Nikolas", 27);
        System.out.println(payment.pay());
    }

    static class MainModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(A.class).to(AImpl.class);
            bind(B.class).to(BImpl.class);
            //bindInterceptor(Matchers.any(), Matchers.annotatedWith(Intercept.class), new Interceptor());
            install(new FactoryModuleBuilder()
                    .implement(Payment.class, RealPayment.class)
                    .build(PaymentFactory.class));
        }
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Intercept {
}

class Interceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        System.out.println("Method intercepted");
        return methodInvocation.proceed();
    }
}

interface PaymentFactory {
    public Payment create(String name, int age);
}

interface Payment {
    public String pay();
}

class RealPayment implements Payment {
    private A a_;
    private B b_;
    private String name_;
    private int age_;

    @Inject
    public RealPayment(A a, B b, @Assisted String name, @Assisted int age) {
        a_ = a;
        b_ = b;
        name_ = name;
        age_ = age;
    }

    @Override
    public String pay() {
        return a_.test() + ", " + b_.test() + ", " + name_ + ", " + age_;
    }
}

interface A {
    public String test();
}

class AImpl implements A {
    @Intercept
    @Override
    public String test() {
        return "AImpl";
    }
}

interface B {
    public String test();
}

class BImpl implements B {
    @Intercept
    @Override
    public String test() {
        return "BImpl";
    }
}
