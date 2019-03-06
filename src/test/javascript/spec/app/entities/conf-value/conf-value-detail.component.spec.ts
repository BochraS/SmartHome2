/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CraTestModule } from '../../../test.module';
import { ConfValueDetailComponent } from 'app/entities/conf-value/conf-value-detail.component';
import { ConfValue } from 'app/shared/model/conf-value.model';

describe('Component Tests', () => {
    describe('ConfValue Management Detail Component', () => {
        let comp: ConfValueDetailComponent;
        let fixture: ComponentFixture<ConfValueDetailComponent>;
        const route = ({ data: of({ confValue: new ConfValue(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [ConfValueDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ConfValueDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ConfValueDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.confValue).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
