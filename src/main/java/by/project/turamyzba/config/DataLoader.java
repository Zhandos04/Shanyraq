package by.project.turamyzba.config;

import by.project.turamyzba.entities.anketa.Option;
import by.project.turamyzba.entities.anketa.Question;
import by.project.turamyzba.repositories.anketa.OptionRepository;
import by.project.turamyzba.repositories.anketa.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final QuestionRepository questionRepository;

    private final OptionRepository optionRepository;

    @Override
    public void run(String... args) {
        if (questionRepository.count() == 0) {

            // Вопрос 1
            Question q1 = new Question();
            q1.setText("Какой ваш обычный распорядок дня и каковы ваши жизненные приоритеты?");
            q1 = questionRepository.save(q1); // Сохраняем вопрос перед добавлением вариантов
            optionRepository.saveAll(List.of(
                    new Option("Я провожу весь день дома, работаю/учусь дистанционно. Мне важно иметь спокойную обстановку дома.", q1),
                    new Option("Я бываю дома только по вечерам, так как учусь/работаю весь день. Мне не принципиально, что происходит дома в мое отсутствие.", q1),
                    new Option("Мне без разницы.", q1)
            ));

            // Вопрос 2
            Question q2 = new Question();
            q2.setText("Как вы относитесь к религиозным практикам и традициям?");
            q2 = questionRepository.save(q2);
            optionRepository.saveAll(List.of(
                    new Option("Я соблюдаю религиозные практики и традиции. Хотел бы, чтобы мой сожитель с уважением относился к этому.", q2),
                    new Option("Я не практикую религию и не следую традициям. Предпочел бы, чтобы мой сожитель не настаивал на религиозных практиках.", q2),
                    new Option("Мне без разницы.", q2)
            ));

            // Вопрос 3
            Question q3 = new Question();
            q3.setText("Какое у вас отношение к курению и алкогольным напиткам?");
            q3 = questionRepository.save(q3);
            optionRepository.saveAll(List.of(
                    new Option("Я спокойно отношусь к курению и алкоголю, но хотел бы, чтобы это не происходило в общих зонах.", q3),
                    new Option("Я не курю и не употребляю алкоголь и предпочитаю, чтобы со мной жил человек с такими же взглядами.", q3),
                    new Option("Мне без разницы.", q3)
            ));

            // Вопрос 4
            Question q4 = new Question();
            q4.setText("Какое ваше отношение к гостям в доме?");
            q4 = questionRepository.save(q4);
            optionRepository.saveAll(List.of(
                    new Option("Не против, если сожитель часто приглашает гостей.", q4),
                    new Option("Я бываю дома только по вечерам, так как учусь/работаю весь день. Мне не принципиально, что происходит дома в мое отсутствие.", q4)
            ));

            // Вопрос 5
            Question q5 = new Question();
            q5.setText("Как вы относитесь к домашним животным?");
            q5 = questionRepository.save(q5);
            optionRepository.saveAll(List.of(
                    new Option("Не против, если у сожителя есть домашние животные.", q5),
                    new Option("Предпочитаю, чтобы животных не было.", q5)
            ));

            // Вопрос 6
            Question q6 = new Question();
            q6.setText("Как вы относитесь к разделению бытовых обязанностей?");
            q6 = questionRepository.save(q6);
            optionRepository.saveAll(List.of(
                    new Option("Предпочитаю равное разделение домашних обязанностей.", q6),
                    new Option("Не против взять на себя большую часть домашних обязанностей.", q6)
            ));

            // Вопрос 7
            Question q7 = new Question();
            q7.setText("Хотели бы вы, чтобы вы с сожителем сдружились и стали друзьями?");
            q7 = questionRepository.save(q7);
            optionRepository.saveAll(List.of(
                    new Option("Да, мне было бы приятно подружиться с сожителем и проводить время вместе.", q7),
                    new Option("Нет, предпочитаю, чтобы у нас были просто дружелюбные соседские отношения.", q7)
            ));

            // Вопрос 8
            Question q8 = new Question();
            q8.setText("Какое ваше отношение к шуму и использованию громких устройств (музыка, ТВ)?");
            q8 = questionRepository.save(q8);
            optionRepository.saveAll(List.of(
                    new Option("Не против шума и громких устройств, если это не поздно вечером.", q8),
                    new Option("Предпочитаю тишину и минимальное использование громких устройств.", q8)
            ));

            System.out.println("Анкета успешно загружена в базу данных.");
        } else {
            System.out.println("Данные анкеты уже существуют в базе.");
        }
    }
}
