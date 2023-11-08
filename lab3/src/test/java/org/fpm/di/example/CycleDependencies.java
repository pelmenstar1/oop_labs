package org.fpm.di.example;

import javax.inject.Inject;

public final class CycleDependencies {
    public static class ConstructorSimple {
        public static class A {
            @Inject
            public A(B b) {
            }
        }

        public static class B {
            @Inject
            public B(A a) {
            }
        }
    }

    public static class ConstructorTwoLevel {
        public static class A {
            @Inject
            public A(B b) {
            }
        }

        public static class B {
            @Inject
            public B(C c) {
            }
        }

        public static class C {
            @Inject
            public C(A a) {
            }
        }
    }

    public static class ConstructorTwoLevelInheritance {
        public static class A1 {
        }

        public static class A2 extends A1 {
            @Inject
            public A2(B b) {
            }
        }

        public static class B {
            @Inject
            public B(C c) {
            }
        }

        public static class C {
            @Inject
            public C(A1 a) {
            }
        }
    }

    public static class InjectFields {
        public static class A {
            @Inject
            public C c;

            public A(B b) {
            }
        }

        public static class B {
            public B(C c) {
            }
        }

        public static class C {
            @Inject
            public A a;
        }
    }
}
